package org.unibl.etf.assetmanager.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {
    private List<AssetFull> assets;

    public interface OnItemClickListener {
        void onDeleteClick(AssetFull asset, int position);

        void onItemClick(AssetFull assetFull);
    }

    private AssetAdapter.OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button deleteButton;
        public TextView name;
        public TextView employee;
        public TextView location;
        public TextView date;
        public ImageView imageView;
        public View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            deleteButton = v.findViewById(R.id.btn_del_asset);
            name = v.findViewById(R.id.asset_name_tv);
            employee = v.findViewById(R.id.asset_employee_tv);
            location = v.findViewById(R.id.asset_location_tv);
            date = v.findViewById(R.id.asset_date_tv);
            imageView = v.findViewById(R.id.asset_item_image_view);
        }
    }

//    public Adapter(List<Item> items) {
//        this.items = items;
//    }

    public AssetAdapter(List<AssetFull> items, AssetAdapter.OnItemClickListener listener) {
        this.assets = items;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AssetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // kreiranje novog view-a
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.asset_recycler_item, parent, false);
        AssetAdapter.ViewHolder vh = new AssetAdapter.ViewHolder(v);
        return vh;
    }

    // sets content inside the view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AssetAdapter.ViewHolder holder, final int position) {
        final AssetFull asset = assets.get(position);
        holder.name.setText(asset.getAsset().getName());
        holder.location.setText(asset.getLocation().getName());
        holder.date.setText(asset.getAsset().getCreationDate().toString());

        if(asset.getAsset().getImageURI()!=null && asset.getEmployee()!=null){
            holder.imageView.setImageURI(Uri.parse(asset.getAsset().getImageURI()));
            Log.d("URI",asset.getAsset().getImageURI());
            holder.employee.setText(asset.getEmployee().getFirstName()+" "+asset.getEmployee().getLastName());
        }
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(asset, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(asset);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }
}
