package org.unibl.etf.assetmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.dao.EmployeeDAO;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{
    private List<EmployeeFull> employees;

    public interface OnItemClickListener {
        void onDeleteClick(EmployeeFull employee,int position);
        void onItemClick(EmployeeFull employeeFull);
    }
    private OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstName;
        public TextView lastName;
        public Button deleteButton;
        public View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            firstName = v.findViewById(R.id.employee_first_name);
            lastName= v.findViewById(R.id.employee_last_name);
            deleteButton=v.findViewById(R.id.btn_del_employee);
        }
    }

//    public Adapter(List<Item> items) {
//        this.items = items;
//    }

    public EmployeeAdapter(List<EmployeeFull> items, OnItemClickListener listener) {
        this.employees = items;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // kreiranje novog view-a
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.employee_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // sets content inside the view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final EmployeeFull employee = employees.get(position);
        holder.firstName.setText(employee.getEmployee().getFirstName());
        holder.lastName.setText(employee.getEmployee().getLastName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(employee,holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(employee);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }
}
