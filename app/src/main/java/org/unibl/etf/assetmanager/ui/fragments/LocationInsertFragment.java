package org.unibl.etf.assetmanager.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LocationInsertFragment extends Fragment {
    View root;
    Button insertBtn;
    EditText locationName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_location_insert, container, false);
        locationName=root.findViewById(R.id.location_insert_name_edit);
        insertBtn=root.findViewById(R.id.btn_insert_location);
        insertBtn.setOnClickListener(this::onInsertClickListener);
        return root;
    }
    private void onInsertClickListener(View view){
        String loc=locationName.getText().toString();
        if(!loc.isEmpty()){
            ExecutorService executorService= Executors.newSingleThreadExecutor();
            executorService.execute(()->{
                AssetManagerDB.getInstance(root.getContext()).getLocationDAO().insert(loc);
                new Handler(Looper.getMainLooper()).post(()->{
                    Bundle bundle=new Bundle();
                    bundle.putBoolean(Constants.LOCATION_INSERT_SHARE_RESULT_KEY,true);
                    getParentFragmentManager().setFragmentResult(Constants.LOCATION_INSERT_SHARE_RESULT_KEY, bundle);
                    Toast.makeText(root.getContext(), R.string.insert_msg, Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                });
            });
        }else Toast.makeText(root.getContext(), R.string.missing_data_msg, Toast.LENGTH_SHORT).show();
    }
}