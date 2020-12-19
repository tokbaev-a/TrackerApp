package knigh4ttk.application.trackerapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import knigh4ttk.application.trackerapp.Item.ItemAdapter;
import knigh4ttk.application.trackerapp.R;

public class HistoryFragment extends Fragment implements HistoryContract.View {

    HistoryPresenter presenter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        setViewItems();
    }

    @Override
    public void onStart() {
        super.onStart();
        setViewItems();
    }

    private void setViewItems() {
        presenter = new HistoryPresenter(this, getContext());
        ItemAdapter adapter = new ItemAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItems(presenter.getAllItems());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure you want to delete all items?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (presenter.getAllItems() != null) {
                        presenter.deleteAll();
                        setViewItems();
                    }
                    Toast.makeText(getContext(), "All items are deleted", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
