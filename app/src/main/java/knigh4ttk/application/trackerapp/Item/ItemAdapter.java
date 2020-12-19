package knigh4ttk.application.trackerapp.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import knigh4ttk.application.trackerapp.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    List<Item> mItems = new ArrayList<>();
    Context context;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Item> items) {
        if (items != null) {
            this.mItems.clear();
            this.mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView distance;
        TextView time;
        TextView speed;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_tv_item);
            distance = itemView.findViewById(R.id.distance_tv_item);
            time = itemView.findViewById(R.id.time_tv_item);
            speed = itemView.findViewById(R.id.speed_tv_item);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Item item) {
            date.setText(item.getDate());
            distance.setText(item.getDistance() + " km");
            time.setText(item.getTime());
            speed.setText(item.getSpeed() + " /km");
        }
    }
}
