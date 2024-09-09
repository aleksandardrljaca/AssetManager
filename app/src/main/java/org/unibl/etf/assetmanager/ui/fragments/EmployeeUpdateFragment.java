package org.unibl.etf.assetmanager.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import org.unibl.etf.assetmanager.MainActivity;
import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.util.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmployeeUpdateFragment extends Fragment {
    private EmployeeFull employee;
    private EditText editFirstName;
    private EditText editLastName;
    private Button updateBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
            employee=(EmployeeFull) getArguments().getSerializable(Constants.EMPLOYEE_UPDATE_SHARE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employee_update, container, false);
        editFirstName = root.findViewById(R.id.update_first_name_edit);
        editLastName = root.findViewById(R.id.update_last_name_edit);
        editFirstName.setText(employee.getEmployee().getFirstName());
        editLastName.setText(employee.getEmployee().getLastName());
        updateBtn = root.findViewById(R.id.btn_update_employee);

        updateBtn.setOnClickListener(view -> {
            String newFirstName = editFirstName.getText().toString();
            String newLastName = editLastName.getText().toString();
            String oldFirstName = employee.getEmployee().getFirstName();
            String oldLastName = employee.getEmployee().getLastName();
            if (!newFirstName.isEmpty() && !newLastName.isEmpty()
                    && (!oldFirstName.equals(newFirstName) || !oldLastName.equals(newLastName))) {
                Long id = employee.getEmployee().getId();
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    AssetManagerDB.getInstance(root.getContext()).getEmployeeDAO().update(id, newFirstName, newLastName);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Bundle result = new Bundle();
                        result.putBoolean(Constants.EMPLOYEE_UPDATE_SHARE_RESULT_KEY, true);
                        getParentFragmentManager().setFragmentResult(Constants.EMPLOYEE_UPDATE_SHARE_RESULT_KEY, result);
                        Toast.makeText(root.getContext(), R.string.update_msg, Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    });
                });
            } else Toast.makeText(root.getContext(), R.string.cannot_update_msg, Toast.LENGTH_SHORT).show();

    });

        return root;
}
}

