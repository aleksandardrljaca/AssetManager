package org.unibl.etf.assetmanager.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.util.Constants;

public class EmployeeDetailsFragment extends Fragment {

    private EmployeeFull employee;
    private TextView firstName;
    private TextView lastName;
    private Button liabilitiesBtn;
    private View root;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            employee= (EmployeeFull) getArguments().getSerializable(Constants.EMPLOYEE_DETAILS_SHARE_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_employee_details, container, false);
        firstName=root.findViewById(R.id.details_first_name);
        lastName=root.findViewById(R.id.details_last_name);
        liabilitiesBtn=root.findViewById(R.id.btn_liabilties_employee);
        liabilitiesBtn.setOnClickListener(this::onLiabilitiesClick);
        if(employee!=null){
            Log.d("ACO","EMPLOYEE NIJE NULL");
            firstName.setText(employee.getEmployee().getFirstName());
            lastName.setText(employee.getEmployee().getLastName());
        }else Log.d("ACO","EMPLOYEE JE NULL");
       return root;
    }
    private void onLiabilitiesClick(View view){
        Bundle bundle=new Bundle();
        bundle.putLong("asset_employeeId",employee.getEmployee().getId());
        Navigation.findNavController(root).navigate(R.id.assetFragment,bundle);
    }
}
