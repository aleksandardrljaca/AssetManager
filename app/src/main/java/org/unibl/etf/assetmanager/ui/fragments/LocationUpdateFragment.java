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
import org.unibl.etf.assetmanager.db.model.Location;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LocationUpdateFragment extends Fragment {
    private Location location;
    private EditText editName;
    private Button updateBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
            location=(Location) getArguments().getSerializable(Constants.LOCATION_UPDATE_SHARE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location_update, container, false);
        editName = root.findViewById(R.id.location_update_name_edit);
        editName.setText(location.getName());
        updateBtn = root.findViewById(R.id.btn_update_location);

        updateBtn.setOnClickListener(view -> {
            String newName = editName.getText().toString();
            String oldName = location.getName();

            if (!newName.equals(oldName)) {
                Long id = location.getId();
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    AssetManagerDB.getInstance(root.getContext()).getLocationDAO().update(id, newName);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Bundle result = new Bundle();
                        result.putBoolean(Constants.LOCATION_UPDATE_SHARE_RESULT_KEY, true);
                        getParentFragmentManager().setFragmentResult(Constants.LOCATION_UPDATE_SHARE_RESULT_KEY, result);
                        Toast.makeText(root.getContext(), R.string.update_msg, Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    });
                });
            } else Toast.makeText(root.getContext(), R.string.cannot_update_msg, Toast.LENGTH_SHORT).show();

        });

        return root;
    }
}