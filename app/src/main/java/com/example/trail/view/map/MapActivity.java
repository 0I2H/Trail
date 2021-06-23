package com.example.trail.view.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityMapBinding;
import com.example.trail.network.helper.NetworkHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapActivity extends BaseActivity<ActivityMapBinding, MapViewModel> implements OnMapReadyCallback {

    public static final String TAG = "MapActivity";

    MapViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityMapBinding binding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public MapViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this))
                .get(MapViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initMap();

        observeViewModel();
    }


    @Override
    public void observeViewModel() {

    }


    // Get a handle to the fragment and register the callback.
    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
//        googleMap.setMapStyle(GoogleMap.MAP_TYPE_SATELLITE)
    }

    // todo 나중에 이걸로 대체
    // https://developers.google.com/maps/documentation/android-sdk/configure-map#runtime-settings
//    // Update the map configuration at runtime.
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // Set the map coordinates to Kyoto Japan.
//        LatLng kyoto = new LatLng(35.00116, 135.7681);
//        // Set the map type to Hybrid.
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        // Add a marker on the map coordinates.
//        googleMap.addMarker(new MarkerOptions()
//                .position(kyoto)
//                .title("Kyoto"));
//        // Move the camera to the map coordinates and zoom in closer.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kyoto));
//        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        // Display traffic.
//        googleMap.setTrafficEnabled(true);
//
//    }
}
