package org.unibl.etf.assetmanager.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.unibl.etf.assetmanager.R;


public class HomeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_home, container, false);
        Button employeesBtn=root.findViewById(R.id.employees_btn);
        employeesBtn.setOnClickListener(this::onEmployeesClick);
        Button settingsBtn=root.findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(this::onSettingsClick);
        Button aboutBtn=root.findViewById(R.id.about_btn);
        aboutBtn.setOnClickListener(this::onAboutClick);
        Button locationsBnt=root.findViewById(R.id.locations_btn);
        locationsBnt.setOnClickListener(this::onLocationsClick);
        Button assetsBnt=root.findViewById(R.id.assets_btn);
        assetsBnt.setOnClickListener(this::onAssetsClick);
        Button inventoryListBtn=root.findViewById(R.id.inventory_list_btn);
        inventoryListBtn.setOnClickListener(this::onInventoryListClick);
        return root;
    }
    public void onAboutClick(View view){
        Navigation.findNavController(view).navigate(R.id.aboutFragment);
    }
    public void onSettingsClick(View view){
        Navigation.findNavController(view).navigate(R.id.settingsFragment);
    }
    public void onEmployeesClick(View view){
        Navigation.findNavController(view).navigate(R.id.employeeFragment);
    }
    public void onLocationsClick(View view){
        Navigation.findNavController(view).navigate(R.id.locationsFragment);
    }
    public void onAssetsClick(View view){
        Navigation.findNavController(view).navigate(R.id.assetFragment);
    }
    public void onInventoryListClick(View view){
        Navigation.findNavController(view).navigate(R.id.inventoryListFragment);
    }

}