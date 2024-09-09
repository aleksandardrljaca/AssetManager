package org.unibl.etf.assetmanager.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.unibl.etf.assetmanager.R;
import org.unibl.etf.assetmanager.db.AssetManagerDB;
import org.unibl.etf.assetmanager.db.model.AssetFull;
import org.unibl.etf.assetmanager.db.model.EmployeeFull;
import org.unibl.etf.assetmanager.db.model.Location;
import org.unibl.etf.assetmanager.util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationDetailsFragment extends Fragment {
    private Location location;
    View root;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Address address = getAddress(location.getName());
            if (location != null && address != null) {
                LatLng city = new LatLng(address.getLatitude(), address.getLongitude());
                Marker marker=googleMap.addMarker(new MarkerOptions().position(city));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 10));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Bundle bundle=new Bundle();
                        bundle.putLong("asset_locationId",location.getId());
                        Navigation.findNavController(root).navigate(R.id.assetFragment,bundle);
                        return true;
                    }
                });
            }


        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(getArguments()!=null){
            location= (Location) getArguments().getSerializable(Constants.LOCATION_DETAILS_SHARE_KEY);
        }
        root =inflater.inflate(R.layout.fragment_location_details, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    private Address getAddress(String location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Address address = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty())
                address = addresses.get(0);
        } catch (IOException e) {
            address = null;
        }

        return address;
    }
}