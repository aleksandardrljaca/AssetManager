package org.unibl.etf.assetmanager.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationAutoCompleteAdapter extends ArrayAdapter<Location> implements Filterable {
    private List<Location> locationList;
    private List<Location> filteredLocationList;

    public LocationAutoCompleteAdapter(@NonNull Context context, @NonNull List<Location> locationList) {
        super(context, android.R.layout.simple_dropdown_item_1line, locationList);
        this.locationList = new ArrayList<>(locationList);
        this.filteredLocationList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return locationFilter;
    }

    private Filter locationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredLocationList.clear();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filteredLocationList.addAll(locationList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Location location: locationList) {
                    if (location.getName().toLowerCase().contains(filterPattern)) {
                        filteredLocationList.add(location);
                    }
                }
            }

            results.values = filteredLocationList;
            results.count = filteredLocationList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List<Location>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Location location = (Location) resultValue;
            return location.getName();
        }
    };
}