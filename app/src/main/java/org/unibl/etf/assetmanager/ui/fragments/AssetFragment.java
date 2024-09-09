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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.adapter.AssetAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AssetFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<AssetFull> assets = new ArrayList<>();
    List<AssetFull> filteredAssets=new ArrayList<>();
    AssetManagerDB db;
    FloatingActionButton addAssetBtn;
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_asset, container, false);
        setResultListener(Constants.ASSET_UPDATE_SHARE_RESULT_REQ_KEY,Constants.ASSET_UPDATE_SHARE_RESULT_KEY);
        setResultListener(Constants.ASSET_INSERT_SHARE_RESULT_REQ_KEY,Constants.ASSET_INSERT_SHARE_RESULT_KEY);
        searchView=root.findViewById(R.id.asset_search_view);
        registerSearchViewListener();
        recyclerView = root.findViewById(R.id.asset_recycler);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        addAssetBtn=root.findViewById(R.id.asset_floating_btn);
        addAssetBtn.setOnClickListener(this::addAssetClick);
        adapter = new AssetAdapter(filteredAssets, new AssetAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(AssetFull asset, int position) {
                ExecutorService executorService= Executors.newSingleThreadExecutor();
                executorService.execute(()->{
                   db.getAssetDAO().delete(asset.getAsset().getId());
                    new Handler(Looper.getMainLooper()).post(()->{
                        assets.remove(asset);
                        filteredAssets.remove(asset);
                        adapter.notifyItemRemoved(position);
                    });
                });
            }

            @Override
            public void onItemClick(AssetFull asset) {
                new AlertDialog.Builder(root.getContext())
                        .setTitle(getString(R.string.context_title))
                        .setItems(new String[]{getString(R.string.context_update),getString(R.string.context_details)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        shareData(Constants.SHARE_DATA_UPDATE_OP,asset,R.id.assetInsertFragment);
                                        break;
                                    case 1:
                                        shareData(Constants.SHARE_DATA_DETAILS_OP,asset,R.id.assetInsertFragment);
                                        break;
                                }
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
        loadAssets();
        // Inflate the layout for this fragment
        return root;
    }


    private void loadAssets() {
        db = AssetManagerDB.getInstance(root.getContext());
        ExecutorService executorService=Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            List<AssetFull> list=null;
            if(getArguments()!=null){
                if(getArguments().getLong(Constants.ASSET_LOCATION_ID_BUNDLE_KEY)!=0)
                    list = db.getAssetDAO().getAllAssetsByLocationId(getArguments().getLong(Constants.ASSET_LOCATION_ID_BUNDLE_KEY));
                else if(getArguments().getLong(Constants.ASSET_EMPLOYEE_ID_BUNDLE_KEY)!=0){
                    list=db.getAssetDAO().getAllAssetsByEmployeeId(getArguments().getLong(Constants.ASSET_EMPLOYEE_ID_BUNDLE_KEY));
                }
            }else list=db.getAssetDAO().getAllAssets();
            updateAssets(list);
        });
    }
    private void updateAssets(List<AssetFull> list) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (list != null && !list.isEmpty()) {
                assets.clear();
                assets.addAll(list);
                filteredAssets.clear();
                filteredAssets.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void shareData(String operation,AssetFull asset,int id){
        Bundle result = new Bundle();
        result.putString(Constants.SHARE_DATA_OPERATION_KEY,operation);
        result.putSerializable(Constants.SHARE_ASSET_KEY,asset);
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
    private void addAssetClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_assetFragment_to_assetInsertFragment);
    }
    private void doSearch(String word){
        filteredAssets.clear();
        if(word.trim().isEmpty()){
            filteredAssets.addAll(assets);
        }else {
            assets.stream()
                    .filter(e->e.getAsset().getName().toLowerCase().startsWith(word.toLowerCase())
                            || e.getAsset().getName().toUpperCase().startsWith(word.toUpperCase())
                            || e.getLocation().getName().toLowerCase().startsWith(word.toLowerCase())
                            || e.getLocation().getName().toUpperCase().startsWith(word.toUpperCase()))
                    .forEach(e->filteredAssets.add(e));
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