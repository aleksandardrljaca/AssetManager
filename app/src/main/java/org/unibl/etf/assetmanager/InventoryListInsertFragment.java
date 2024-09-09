package org.unibl.etf.assetmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.InventoryListFull;
import org.unibl.etf.assetmanager.util.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InventoryListInsertFragment extends Fragment {

    View root;
    AutoCompleteTextView barcodeTV;
    EditText nameET;
    Button scanBtn;
    Button findBtn;
    Button saveBtn;
    AssetManagerDB db;
    boolean isSaved = false;
    private Long listId;
    private boolean findClicked=false;
    private boolean beenToInsertFragment=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_inventory_list_insert, container, false);
        db = AssetManagerDB.getInstance(root.getContext());
        barcodeTV = root.findViewById(R.id.barcode_insert_item_tv);
        nameET = root.findViewById(R.id.inv_list_name_et);
        scanBtn = root.findViewById(R.id.scan_barcode_inv_btn);
        scanBtn.setOnClickListener(this::onScanClick);
        findBtn = root.findViewById(R.id.find_btn_inv_list);
        saveBtn = root.findViewById(R.id.inv_list_save_btn);
        findBtn.setOnClickListener(this::onFindClick);
        saveBtn.setOnClickListener(this::onSaveClick);
        generateListId();
        isSaved = false;
        findClicked=false;
        return root;
    }

    private void onScanClick(View view) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(InventoryListInsertFragment.this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE && data != null) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                String resultText = result.getContents();
                if (resultText != null) {
                    barcodeTV.setText(resultText);
                    onFindClick(root);
                    barcodeTV.getText().clear();
                } else {
                    barcodeTV.setText("Scan canceled");
                }
            }
        }
    }

    private void onFindClick(View view) {
        if (!barcodeTV.getText().toString().isEmpty()) {
            try {
                Long barcode = Long.parseLong(barcodeTV.getText().toString());
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    AssetFull asset = db.getAssetDAO().getAllByBarcode(barcode);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (asset != null) {
                            findClicked=true;
                            beenToInsertFragment=true;
                            barcodeTV.getText().clear();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ser", asset);
                            bundle.putLong("listId", listId);
                            Navigation.findNavController(root).navigate(R.id.inventoryListItemInsertFragment, bundle);
                        } else
                            Toast.makeText(root.getContext(), R.string.no_asset_barcode_msg, Toast.LENGTH_SHORT).show();
                    });
                });
            } catch (Exception e) {
                Toast.makeText(root.getContext(), R.string.non_valid_input_msg, Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(root.getContext(), R.string.barcode_not_entered_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        // if flag is false delete all that contains this listId
        if (!isSaved && !findClicked) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                db.getInventoryItemDAO().deleteAllFromInventoryList(listId);
                db.getInventoryListDAO().delete(listId);
            });
        }
    }

    private void onSaveClick(View view) {
        //check if name is selected
        // update list name

        if (!nameET.getText().toString().isEmpty()) {
            String name = nameET.getText().toString();
            Executors.newSingleThreadExecutor().execute(()->{
                db.getInventoryListDAO().update(listId, name);
                new Handler(Looper.getMainLooper()).post(()->{
                    // send to previous fragment flag true to notify adapter
                    isSaved = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.INVENTORY_LIST_INSERT_SHARE_RESULT_KEY, true);
                    getParentFragmentManager().setFragmentResult(Constants.INVENTORY_LIST_INSERT_SHARE_RESULT_REQ_KEY, bundle);
                    getParentFragmentManager().popBackStack();
                });
            });
        }

    }

    private void generateListId() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // insert with null name -> when save clicked update list name
            if(!beenToInsertFragment)
                db.getInventoryListDAO().insert(null);
            // set listId to newly created id for the list
            listId = db.getInventoryListDAO().getMaxId();
        });
    }
}