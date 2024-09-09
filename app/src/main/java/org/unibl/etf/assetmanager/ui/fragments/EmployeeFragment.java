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
import org.unibl.etf.assetmanager.adapter.EmployeeAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.util.Constants;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class EmployeeFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<EmployeeFull> employees = new ArrayList<>();
    List<EmployeeFull> filteredEmployees=new ArrayList<>();
    AssetManagerDB db;
    FloatingActionButton addEmployeeBtn;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_employee, container, false);
        setResultListener(Constants.EMPLOYEE_UPDATE_SHARE_RESULT_REQ_KEY,Constants.EMPLOYEE_UPDATE_SHARE_RESULT_KEY);
        setResultListener(Constants.EMPLOYEE_INSERT_SHARE_RESULT_REQ_KEY,Constants.EMPLOYEE_INSERT_SHARE_RESULT_KEY);
        searchView=root.findViewById(R.id.employee_search_view);
        registerSearchViewListener();
        recyclerView = root.findViewById(R.id.employee_recycler);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        addEmployeeBtn=root.findViewById(R.id.employees_floating_btn);
        addEmployeeBtn.setOnClickListener(this::addEmployeeClick);
        adapter = new EmployeeAdapter(filteredEmployees, new EmployeeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(EmployeeFull employee, int position) {
                    ExecutorService executorService=Executors.newSingleThreadExecutor();
                    executorService.execute(()->{
                      db.getEmployeeDAO().delete(employee.getEmployee().getId());
                      new Handler(Looper.getMainLooper()).post(()->{
                          employees.remove(employee);
                          filteredEmployees.remove(employee);
                          adapter.notifyItemRemoved(position);
                      });
                    });
            }

            @Override
            public void onItemClick(EmployeeFull employeeFull) {
                new AlertDialog.Builder(root.getContext())
                        .setTitle(getString(R.string.context_title))
                        .setItems(new String[]{getString(R.string.context_update),getString(R.string.context_details)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        shareData(Constants.EMPLOYEE_UPDATE_SHARE_KEY,employeeFull,R.id.action_employeeFragment_to_updateEmployeeFragment);
                                        break;
                                    case 1:
                                        shareData(Constants.EMPLOYEE_DETAILS_SHARE_KEY,employeeFull,R.id.action_employeeFragment_to_employeeDetailsFragment);
                                        break;
                                }
                            }
                        }).show();
            }
        });
         recyclerView.setAdapter(adapter);
        loadEmployees();
        // Inflate the layout for this fragment
        return root;
    }


    private void loadEmployees() {
        db = AssetManagerDB.getInstance(root.getContext());
        ExecutorService executorService=Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            List<EmployeeFull> list=db.getEmployeeDAO().getAllEmployees();
            new Handler(Looper.getMainLooper()).post(()->{
                if(list!=null && !list.isEmpty()){
                    employees.clear();
                    employees.addAll(list);
                    filteredEmployees.clear();
                    filteredEmployees.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
    private void shareData(String key,EmployeeFull employee,int id){
        Bundle result = new Bundle();
        result.putSerializable(key,employee);
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
    private void addEmployeeClick(View view){
        Navigation.findNavController(view).navigate(R.id.action_employeeFragment_to_employeeInsertFragment);
    }
    private void doSearch(String word){
        filteredEmployees.clear();
        if(word.trim().isEmpty()){
            filteredEmployees.addAll(employees);
        }else {
            employees.stream()
                    .filter(e->e.getEmployee().getFirstName().toLowerCase().startsWith(word.toLowerCase())
                            || e.getEmployee().getLastName().toLowerCase().startsWith(word.toLowerCase())
                            || e.getEmployee().getFirstName().toUpperCase().startsWith(word.toUpperCase())
                            || e.getEmployee().getLastName().toUpperCase().startsWith(word.toUpperCase())).forEach(e->filteredEmployees.add(e));
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