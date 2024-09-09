package org.unibl.etf.assetmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<Location> locations;
    public interface OnItemClickListener {
        void onDeleteClick(Location location, int position);
        void onItemClick(Location location);
    }
    private LocationAdapter.OnItemClickListener listener;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView locationName;
        Button deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName=itemView.findViewById(R.id.location_name_text_view);
            deleteBtn=itemView.findViewById(R.id.delete_location_btn);
        }
    }

    public LocationAdapter(List<Location> locations, LocationAdapter.OnItemClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.location_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        final Location location=locations.get(position);
        holder.locationName.setText(location.getName());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(location,holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
