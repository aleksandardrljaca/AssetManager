package org.unibl.etf.assetmanager.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.unibl.etf.assetmanager.db.model.EmployeeFull;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAutoCompleteAdapter extends ArrayAdapter<EmployeeFull> implements Filterable {
    private List<EmployeeFull> employeeList;
    private List<EmployeeFull> filteredEmployeeList;

    public EmployeeAutoCompleteAdapter(@NonNull Context context, @NonNull List<EmployeeFull> employeeList) {
        super(context, android.R.layout.simple_dropdown_item_1line, employeeList);
        this.employeeList = new ArrayList<>(employeeList);
        this.filteredEmployeeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return employeeFilter;
    }

    private Filter employeeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredEmployeeList.clear();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filteredEmployeeList.addAll(employeeList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (EmployeeFull employee : employeeList) {
                    if ((employee.getEmployee().getFirstName() + " " + employee.getEmployee().getLastName()).toLowerCase().contains(filterPattern)) {
                        filteredEmployeeList.add(employee);
                    }
                }
            }

            results.values = filteredEmployeeList;
            results.count = filteredEmployeeList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List<EmployeeFull>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            EmployeeFull employee = (EmployeeFull) resultValue;
            return employee.getEmployee().getFirstName() + " " + employee.getEmployee().getLastName();
        }
    };
}
