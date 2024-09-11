package org.unibl.etf.assetmanager.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.adapter.EmployeeAdapter;
import org.unibl.etf.assetmanager.adapter.LocationAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationsFragment extends Fragment {

    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Location> locations = new ArrayList<>();
    List<Location> filteredLocations=new ArrayList<>();
    AssetManagerDB db;
    FloatingActionButton addLocationBtn;
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_locations, container, false);
        setResultListener(Constants.LOCATION_UPDATE_SHARE_RESULT_REQ_KEY,Constants.LOCATION_UPDATE_SHARE_RESULT_KEY);
        setResultListener(Constants.LOCATION_INSERT_SHARE_RESULT_REQ_KEY,Constants.LOCATION_INSERT_SHARE_RESULT_KEY);
        searchView=root.findViewById(R.id.locations_search_view);
        registerSearchViewListener();
        recyclerView = root.findViewById(R.id.locations_recycler);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        addLocationBtn=root.findViewById(R.id.locations_floating_btn);
        addLocationBtn.setOnClickListener(this::addLocationClick);
        adapter = new LocationAdapter(filteredLocations, new LocationAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(Location location, int position) {
                ExecutorService executorService= Executors.newSingleThreadExecutor();
                executorService.execute(()->{
                    List<AssetFull> assets=db.getAssetDAO().getAllAssetsByLocationId(location.getId());
                    if(assets!=null && assets.isEmpty()){
                        db.getLocationDAO().delete(location.getId());
                        new Handler(Looper.getMainLooper()).post(()->{
                            locations.remove(location);
                            filteredLocations.remove(location);
                            adapter.notifyItemRemoved(position);
                        });
                    }else new Handler(Looper.getMainLooper()).post(()->{
                        Toast.makeText(root.getContext(), getString(R.string.cannot_delete), Toast.LENGTH_SHORT).show();
                    });
                });
            }

            @Override
            public void onItemClick(Location location) {
                new AlertDialog.Builder(root.getContext())
                        .setTitle(getString(R.string.context_title))
                        .setItems(new String[]{getString(R.string.context_update),getString(R.string.context_details)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        shareData(Constants.LOCATION_UPDATE_SHARE_KEY,location,R.id.action_locationsFragment_to_locationUpdateFragment);
                                        break;
                                    case 1:
                                        shareData(Constants.LOCATION_DETAILS_SHARE_KEY,location,R.id.action_locationsFragment_to_locationDetailsFragment);
                                        break;
                                }
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
        loadLocations();
        // Inflate the layout for this fragment
        return root;
    }


    private void loadLocations() {
        db = AssetManagerDB.getInstance(root.getContext());
        ExecutorService executorService=Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            List<Location> list=db.getLocationDAO().getAllLocations();
            new Handler(Looper.getMainLooper()).post(()->{
                if(list!=null && !list.isEmpty()){
                    locations.clear();
                    locations.addAll(list);
                    filteredLocations.clear();
                    filteredLocations.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
    private void shareData(String key,Location location,int id){
        Bundle result = new Bundle();
        result.putSerializable(key,location);
        Navigation.findNavController(root).navigate(id,result);
    }
    private void setResultListener(String requestKey,String key){
        getParentFragmentManager().setFragmentResultListener(requestKey, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                if(bundle.getBoolean(key)) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void addLocationClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_locationsFragment_to_locationInsertFragment);
    }
    private void doSearch(String word){
        filteredLocations.clear();
        if(word.trim().isEmpty()){
            filteredLocations.addAll(locations);
        }else {
            locations.stream()
                    .filter(l->l.getName().toLowerCase().startsWith(word.toLowerCase())
                            || l.getName().toUpperCase().startsWith(word.toUpperCase())).forEach(l->filteredLocations.add(l));
        }
        adapter.notifyDataSetChanged();
    }
    private void registerSearchViewListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return false;
            }
        });
    }
}