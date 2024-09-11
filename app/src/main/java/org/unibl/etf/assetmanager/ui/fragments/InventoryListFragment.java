package org.unibl.etf.assetmanager.ui.fragments;

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
import org.unibl.etf.assetmanager.adapter.InventoryListAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.InventoryListFull;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InventoryListFragment extends Fragment {

    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<InventoryListFull> inventoryLists = new ArrayList<>();
    List<InventoryListFull> filteredInventoryLists=new ArrayList<>();
    AssetManagerDB db;
    FloatingActionButton addInventoryListBtn;
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_inventory_list, container, false);
        recyclerView=root.findViewById(R.id.inventory_list_recycler);
        addInventoryListBtn=root.findViewById(R.id.inventory_list_floating_btn);
        addInventoryListBtn.setOnClickListener(this::onAddInvListClick);
        searchView=root.findViewById(R.id.inventory_list_search_view);
        registerSearchViewListener();
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        setResultListener(Constants.INVENTORY_LIST_INSERT_SHARE_RESULT_REQ_KEY,Constants.INVENTORY_LIST_INSERT_SHARE_RESULT_KEY);
        adapter=new InventoryListAdapter(filteredInventoryLists, new InventoryListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(InventoryListFull inventoryListFull, int position) {
                ExecutorService executorService= Executors.newSingleThreadExecutor();
                executorService.execute(()->{
                    db.getInventoryItemDAO().deleteAllFromInventoryList(inventoryListFull.getInventoryList().getId());
                    db.getInventoryListDAO().delete(inventoryListFull.getInventoryList().getId());
                    new Handler(Looper.getMainLooper()).post(()->{
                        inventoryLists.remove(inventoryListFull);
                        filteredInventoryLists.remove(inventoryListFull);
                        adapter.notifyItemRemoved(position);
                    });
                });
            }

            @Override
            public void onItemClick(InventoryListFull inventoryListFull) {
                shareData(Constants.INVENTORY_LIST_DETAILS_SHARE_KEY,inventoryListFull.getInventoryList().getId(),R.id.inventoryListDetailsFragment);
            }
        });
        recyclerView.setAdapter(adapter);
        loadInventoryLists();
        return root;
    }
    private void shareData(String key,Long listId,int id){
        Bundle result = new Bundle();
        result.putLong(key,listId);
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
    private void onAddInvListClick(View view){
        Navigation.findNavController(root).navigate(R.id.inventoryListInsertFragment);
    }
    private void loadInventoryLists() {
        db = AssetManagerDB.getInstance(root.getContext());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<InventoryListFull> list = db.getInventoryListDAO().getAllInventoryLists();
            new Handler(Looper.getMainLooper()).post(() -> {
                if (list != null && !list.isEmpty()) {
                    inventoryLists.clear();
                    inventoryLists.addAll(list);
                    filteredInventoryLists.clear();
                    filteredInventoryLists.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
    private void doSearch(String word){
        filteredInventoryLists.clear();
        if(word.trim().isEmpty()){
            filteredInventoryLists.addAll(inventoryLists);
        }else {
            inventoryLists.stream()
                    .filter(l->l.getInventoryList().getName().toLowerCase().startsWith(word.toLowerCase())
                            || l.getInventoryList().getName().toUpperCase().startsWith(word.toUpperCase())).forEach(l->filteredInventoryLists.add(l));
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