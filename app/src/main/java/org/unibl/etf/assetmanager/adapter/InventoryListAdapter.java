package org.unibl.etf.assetmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.model.InventoryListFull;

import java.util.List;

public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListAdapter.ViewHolder> {
    private List<InventoryListFull> inventoryLists;

    public interface OnItemClickListener {
        void onDeleteClick(InventoryListFull inventoryListFull, int position);

        void onItemClick(InventoryListFull inventoryListFull);
    }

    private InventoryListAdapter.OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button deleteButton;
        public TextView name;
        public View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            deleteButton = v.findViewById(R.id.delete_location_btn);
            name = v.findViewById(R.id.location_name_text_view);
        }
    }

//    public Adapter(List<Item> items) {
//        this.items = items;
//    }

    public InventoryListAdapter(List<InventoryListFull> items, InventoryListAdapter.OnItemClickListener listener) {
        this.inventoryLists = items;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InventoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // kreiranje novog view-a
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.location_recycler_item, parent, false);
        InventoryListAdapter.ViewHolder vh = new InventoryListAdapter.ViewHolder(v);
        return vh;
    }

    // sets content inside the view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(InventoryListAdapter.ViewHolder holder, final int position) {
        final InventoryListFull list=inventoryLists.get(position);
        holder.name.setText(list.getInventoryList().getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(list, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryLists.size();
    }
}
