package org.unibl.etf.assetmanager.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.adapter.InventoryItemAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.InventoryItemFull;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InventoryListDetailsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<InventoryItemFull> inventoryList = new ArrayList<>();
    AssetManagerDB db;
    View root;
    Long listId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            listId=getArguments().getLong(Constants.INVENTORY_LIST_DETAILS_SHARE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_inventory_list_details, container, false);
        recyclerView=root.findViewById(R.id.inv_list_details_recycler);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new InventoryItemAdapter(inventoryList);
        recyclerView.setAdapter(adapter);
        loadItems(listId);
        return root;
    }
    private void loadItems(Long id){
        db=AssetManagerDB.getInstance(root.getContext());
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            List<InventoryItemFull> list=db.getInventoryItemDAO().getAllByInventoryListId(id);
            new Handler(Looper.getMainLooper()).post(()->{
                if(list!=null && !list.isEmpty()){
                    inventoryList.clear();
                    inventoryList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}