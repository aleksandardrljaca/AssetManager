package org.unibl.etf.assetmanager.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.Employee;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.InventoryItemFull;
import org.unibl.etf.assetmanager.db.model.InventoryItemFull;
import org.unibl.etf.assetmanager.db.model.Location;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.ViewHolder> {
    private List<InventoryItemFull> inventoryLists;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView employee;
        public TextView location;
        public TextView newEmployee;
        public TextView newLocation;
        public TextView date;
        public ImageView imageView;
        public View root;
        public ViewHolder(View v) {
            super(v);
            root = v;
            name = v.findViewById(R.id.inv_item_name_tv);
            employee=v.findViewById(R.id.inv_item_employee_tv);
            location=v.findViewById(R.id.inv_item_location_tv);
            newEmployee=v.findViewById(R.id.inv_item_new_emp_tv);
            newLocation=v.findViewById(R.id.inv_item_new_loc_tv);
            date=v.findViewById(R.id.inv_item_date_tv);
            imageView=v.findViewById(R.id.inv_item_image_view);
        }
    }

//    public Adapter(List<Item> items) {
//        this.items = items;
//    }

    public InventoryItemAdapter(List<InventoryItemFull> items) {
        this.inventoryLists = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InventoryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // kreiranje novog view-a
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.inventory_item_recycler_item, parent, false);
        InventoryItemAdapter.ViewHolder vh = new InventoryItemAdapter.ViewHolder(v);
        return vh;
    }

    // sets content inside the view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(InventoryItemAdapter.ViewHolder holder, final int position) {
        final InventoryItemFull item=inventoryLists.get(position);
        AssetManagerDB db=AssetManagerDB.getInstance(holder.root.getContext());
        holder.name.setText(item.getAsset().getName());
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Employee employee=db.getEmployeeDAO().getById(item.getAsset().getAsset_employeeId()).getEmployee();
                Location location=db.getLocationDAO().getLocationById(item.getAsset().getAsset_locationId());
                if(employee!=null && location!=null){
                    holder.employee.setText(employee.getFirstName()+" "+employee.getLastName());
                    holder.location.setText(location.getName());
                }

            }
        });
        holder.imageView.setImageURI(Uri.parse(item.getAsset().getImageURI()));
        holder.date.setText(item.getAsset().getCreationDate().toString());
        holder.newLocation.setText(item.getNewLocation().getName());
        holder.newEmployee.setText(item.getNewEmployee().getFirstName()+" "+item.getNewEmployee().getLastName());

    }

    @Override
    public int getItemCount() {
        return inventoryLists.size();
    }
}
