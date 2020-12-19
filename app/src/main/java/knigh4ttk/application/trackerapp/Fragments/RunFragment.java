package knigh4ttk.application.trackerapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import knigh4ttk.application.trackerapp.Item.Item;
import knigh4ttk.application.trackerapp.R;

public class RunFragment extends Fragment implements OnMapReadyCallback, RunContract.View {
    private static final int TAG_CODE_PERMISSION_LOCATION = 0;
    ArrayList<Location> locations = new ArrayList<>();
    FusedLocationProviderClient client;
    Task<Location> locationTask;
    Location currentLocation;
    RunPresenter presenter;
    View view;
    GoogleMap map;
    MapView mapView;
    TextView speedTextView;
    TextView distanceTextView;
    double distance = 0, speed = 0, speedOnTime;
    private long pauseOffset;
    long sTime, dTime;
    String time;
    Chronometer chronometer;
    Button startButton, stopButton;
    private boolean chronometerIsWorking;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.run_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new RunPresenter(this, getContext());

        mapView = view.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        chronometer = view.findViewById(R.id.time_indicator);
        startButton = view.findViewById(R.id.start_button);
        startButton.setVisibility(View.VISIBLE);
        stopButton = view.findViewById(R.id.stop_button);
        stopButton.setVisibility(View.GONE);
        chronometer.setVisibility(View.VISIBLE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChronometer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopButton.getText() == "Stop") {
                    stopChronometer();
                } else if (stopButton.getText() == "Finish") {
                    finishChronometer();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void startChronometer() {
        if (!chronometerIsWorking) {
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            stopButton.setText("Stop");
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            chronometerIsWorking = true;
            getLocation();
            locations.add(currentLocation);
        }
    }


    private void getLocation() {
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    mapView.getMapAsync(RunFragment.this);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void stopChronometer() {
        if (chronometerIsWorking) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometerIsWorking = false;
            startButton.setVisibility(View.VISIBLE);
            startButton.setText("Resume");
            stopButton.setVisibility(View.VISIBLE);
            stopButton.setText("Finish");
        }
        dTime = pauseOffset - sTime;
        long hours = TimeUnit.MILLISECONDS.toHours(dTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(dTime) - hours * 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(dTime) - hours * 60 * 60 - minutes * 60;
        time = hours + "h:" + minutes + "m:" + seconds + "s";
        speedOnTime = (int) (dTime / 1000);
        getLocation();
        locations.add(currentLocation);

        double startLat = locations.get(0).getLatitude();
        double startLong = locations.get(0).getLongitude();
        double endLat = locations.get(1).getLatitude();
        double endLong = locations.get(1).getLongitude();
        float result = 0;
        Location.distanceBetween(startLat, startLong, endLat, endLong, new float[]{result});

        int length = locations.size();
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(Color.RED);
        polygonOptions.strokeWidth(5);
        for (int i = 0; i < length; i++) {
            polygonOptions.add(new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()));
        }
        map.addPolygon(polygonOptions);

        distance = distance + result;
        if (distance == 0) speed = 0;
        else {
            speed = ((distance * 1000) / speedOnTime);
            distanceTextView.setText(distance + " km");
            speedTextView.setText(speed + " m/sec");
        }
    }

    @SuppressLint("SetTextI18n")
    private void finishChronometer() {
        startButton.setText("Start");
        stopButton.setVisibility(View.GONE);
        chronometer.setVisibility(View.VISIBLE);
        stopButton.setText("Stop");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();
        Item item = new Item(distance, time, speed, formatter.format(date));
        presenter.insert(item);
        Toast.makeText(getContext(), "Your run's data is saved", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            client = LocationServices.getFusedLocationProviderClient(getContext());
            locationTask = client.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null) {
                        currentLocation = location;
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                MarkerOptions options = new MarkerOptions().position(latLng).title("I am here");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
            map.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                        TAG_CODE_PERMISSION_LOCATION);
            }
        }

    }
}
