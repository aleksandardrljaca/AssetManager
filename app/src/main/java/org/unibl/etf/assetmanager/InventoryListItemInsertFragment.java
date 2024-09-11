package org.unibl.etf.assetmanager;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.unibl.etf.assetmanager.adapter.EmployeeAutoCompleteAdapter;
import org.unibl.etf.assetmanager.adapter.LocationAutoCompleteAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.Employee;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class InventoryListItemInsertFragment extends Fragment {

    private AssetFull asset;
    private ImageView imageView;
    private EditText nameET;
    private EditText descET;
    private EditText employeeET;
    private EditText locationET;
    private EditText barcodeET;
    private EditText priceET;
    private AutoCompleteTextView newEmployeeTV;
    private AutoCompleteTextView newLocationTV;
    private Button addBtn;
    private View root;
    private AssetManagerDB db;
    private List<EmployeeFull> employees;
    private List<Location> locations;
    private Long listId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            asset= (AssetFull) getArguments().getSerializable("ser");
            listId = getArguments().getLong("listId");
        }
        db = AssetManagerDB.getInstance(getContext());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            employees = db.getEmployeeDAO().getAllEmployees();
            locations = db.getLocationDAO().getAllLocations();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_inventory_list_item_insert, container, false);
        imageView=root.findViewById(R.id.item_insert_imageView);
        nameET=root.findViewById(R.id.item_insert_name_et);
        descET=root.findViewById(R.id.item_insert_desc_et);
        employeeET=root.findViewById(R.id.item_insert_tv_employee);
        locationET=root.findViewById(R.id.item_insert_tv_location);
        barcodeET=root.findViewById(R.id.item_insert_barcode_et);
        priceET=root.findViewById(R.id.item_insert_price_et);
        newEmployeeTV=root.findViewById(R.id.item_insert_new_emp_et);
        newLocationTV=root.findViewById(R.id.item_insert_new_loc_et);
        addBtn=root.findViewById(R.id.btn_insert_item);
        addBtn.setOnClickListener(this::onAddClick);
        fillInfo();
        return root;
    }
    private void fillInfo(){
        if(asset!=null){
            imageView.setImageURI(Uri.parse(asset.getAsset().getImageURI()));
            nameET.setText(asset.getAsset().getName());
            descET.setText(asset.getAsset().getDescription());
            employeeET.setText(asset.getEmployee().getFirstName()+" "+asset.getEmployee().getLastName());
            locationET.setText(asset.getLocation().getName());
            barcodeET.setText(asset.getAsset().getBarcode().toString());
            priceET.setText(asset.getAsset().getPrice().toString());
        }
        if(employees!=null && locations!=null){
            newEmployeeTV.setAdapter(new EmployeeAutoCompleteAdapter(root.getContext(),employees));
            newLocationTV.setAdapter(new LocationAutoCompleteAdapter(root.getContext(),locations));
        }
    }
    private void onAddClick(View view){
        // check if all fields are filled
        // db.getInventoryItemDAO().insert(listId,fields...)
        if(!newEmployeeTV.getText().toString().isEmpty() && newEmployeeTV.getText().toString().contains(" ") && !newLocationTV.getText().toString().isEmpty()){
            Executors.newSingleThreadExecutor().execute(()->{
                String[] name=newEmployeeTV.getText().toString().split(" ");
                String location=newLocationTV.getText().toString();
                Long newEmployeeId=db.getEmployeeDAO().getIdByName(name[0],name[1]);
                Long newLocationId=db.getLocationDAO().getIdByName(location);
                if(newEmployeeId!=null && newLocationId!=null){
                    db.getInventoryItemDAO().insert(asset.getAsset().getId(),newLocationId,newEmployeeId,listId);
                    // pop back stack
                }
                new Handler(Looper.getMainLooper()).post(()->getParentFragmentManager().popBackStack());
            });
        }else Toast.makeText(root.getContext(),R.string.missing_data_msg,Toast.LENGTH_SHORT).show();
    }
}