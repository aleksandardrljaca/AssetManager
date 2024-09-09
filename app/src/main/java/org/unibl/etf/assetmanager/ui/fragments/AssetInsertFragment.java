package org.unibl.etf.assetmanager.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.adapter.EmployeeAutoCompleteAdapter;
import org.unibl.etf.assetmanager.adapter.LocationAutoCompleteAdapter;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AssetInsertFragment extends Fragment {
    View root;
    ImageView assetImage;
    Button imageAssetBtn;
    EditText nameET;
    EditText descET;
    AutoCompleteTextView employeeTV;
    AutoCompleteTextView locationTV;
    EditText barcodeET;
    Button scanBarcodeBtn;
    EditText priceET;
    Button insertBtn;
    List<EmployeeFull> employees;
    List<Location> locations;
    AssetManagerDB db;
    private static final int PICK_IMAGE = 100;
    private Uri assetImageUri;
    private boolean isUpdateMode;
    private boolean isDetailsMode;
    AssetFull assetShared;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        root = inflater.inflate(R.layout.fragment_asset_insert, container, false);
        assetImage = root.findViewById(R.id.asset_insert_imageView);
        imageAssetBtn = root.findViewById(R.id.insert_capture_btn);
        nameET = root.findViewById(R.id.asset_insert_name_et);
        descET = root.findViewById(R.id.asset_insert_desc_et);
        employeeTV = root.findViewById(R.id.asset_insert_tv_employee);
        locationTV = root.findViewById(R.id.asset_insert_tv_location);
        barcodeET = root.findViewById(R.id.asset_insert_barcode_et);
        scanBarcodeBtn = root.findViewById(R.id.asset_insert_barcode_scan_btn);
        priceET = root.findViewById(R.id.asset_insert_price_et);
        insertBtn = root.findViewById(R.id.btn_insert_asset);
        employeeTV.setAdapter(new EmployeeAutoCompleteAdapter(root.getContext(), employees));
        locationTV.setAdapter(new LocationAutoCompleteAdapter(root.getContext(), locations));
        scanBarcodeBtn.setOnClickListener(this::onScanClick);
        imageAssetBtn.setOnClickListener(this::onChooseImageClick);
        insertBtn.setOnClickListener(this::onInsertClick);
        prepareUpdateDetails();
        return root;
    }
    private void onScanClick(View view){
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(AssetInsertFragment.this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.initiateScan();
    }
    private void onChooseImageClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    private void onInsertClick(View view) {
        if (!nameET.getText().toString().isEmpty() && !descET.getText().toString().isEmpty() &&
                !employeeTV.getText().toString().isEmpty() && !locationTV.getText().toString().isEmpty() && !nameET.getText().toString().isEmpty()
                && !barcodeET.getText().toString().isEmpty() && !priceET.getText().toString().isEmpty()) {
            String assetName = nameET.getText().toString();
            String assetDesc = descET.getText().toString();
            String[] employee = employeeTV.getText().toString().split(" ");
            Long barCode = Long.parseLong(barcodeET.getText().toString());
            Double price = Double.parseDouble(priceET.getText().toString());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                Long employeeId = db.getEmployeeDAO().getIdByName(employee[0], employee[1]);
                Long locationId = db.getLocationDAO().getIdByName(locationTV.getText().toString());
                if (locationId != null && employeeId != null && assetImageUri!=null) {
                    Location location=db.getLocationDAO().getLocationById(locationId);
                    if(isUpdateMode) db.getAssetDAO().update(assetShared.getAsset().getId(),assetName, assetDesc, barCode,
                            price, new Date(), assetImageUri.toString(), locationId, employeeId);
                    else if(!isUpdateMode && !isDetailsMode) db.getAssetDAO().insert(assetName, assetDesc, barCode, price, new Date(), assetImageUri.toString(), locationId, employeeId);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if(isUpdateMode || !isDetailsMode){
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Constants.ASSET_INSERT_SHARE_RESULT_KEY, true);
                            getParentFragmentManager().setFragmentResult(Constants.ASSET_INSERT_SHARE_RESULT_REQ_KEY, bundle);
                            String msg="";
                            if(isUpdateMode)
                                msg=getString(R.string.update_msg);
                            else msg=getString(R.string.insert_msg);
                            Toast.makeText(root.getContext(), msg, Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                        else {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(Constants.LOCATION_DETAILS_SHARE_KEY,location);
                            Navigation.findNavController(root).navigate(R.id.locationDetailsFragment,bundle);
                        }
                    });
                }

            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE && data != null) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                String resultText = result.getContents();
                if (resultText != null) {
                    barcodeET.setText(resultText);
                } else {
                    barcodeET.setText(getString(R.string.scan_canceled_msg));
                }
            }
        }  else if (requestCode == PICK_IMAGE && data != null) {
            assetImageUri=data.getData();
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).into(assetImage);
        }
    }
   private void prepareUpdateDetails(){
       if(getArguments()!=null){
           String operation=getArguments().getString(Constants.SHARE_DATA_OPERATION_KEY);
           assetShared=(AssetFull) getArguments().getSerializable(Constants.SHARE_ASSET_KEY);
           nameET.setText(assetShared.getAsset().getName());
           descET.setText(assetShared.getAsset().getDescription());
           employeeTV.setText(assetShared.getEmployee().getFirstName()+" "+assetShared.getEmployee().getLastName());
           locationTV.setText(assetShared.getLocation().getName());
           barcodeET.setText(assetShared.getAsset().getBarcode().toString());
           priceET.setText(assetShared.getAsset().getPrice().toString());
           assetImage.setImageURI(Uri.parse(assetShared.getAsset().getImageURI()));
           if(operation.equals(Constants.SHARE_DATA_DETAILS_OP)){
               isDetailsMode=true;
               nameET.setFocusable(false);
               descET.setFocusable(false);
               employeeTV.setFocusable(false);
               locationTV.setFocusable(false);
               barcodeET.setFocusable(false);
               scanBarcodeBtn.setVisibility(View.INVISIBLE);
               priceET.setFocusable(false);
               imageAssetBtn.setVisibility(View.INVISIBLE);
               insertBtn.setText(R.string.locations_btn_text);
               if(getActivity()!=null){
                   ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.all_details_fragment_name);
               }
               assetImageUri=Uri.parse(assetShared.getAsset().getImageURI());
           }
           else if(operation.equals(Constants.SHARE_DATA_UPDATE_OP)){
               //override insert button behaviour
               if(getActivity()!=null){
                   ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.all_update_fragment_name);
               }
               isUpdateMode=true;
               assetImageUri=Uri.parse(assetShared.getAsset().getImageURI());
               insertBtn.setText(R.string.asset_update_btn_lbl);
           }
       }
   }


}