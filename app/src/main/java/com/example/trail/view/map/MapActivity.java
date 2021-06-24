package com.example.trail.view.map;

import android.content.Intent;
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
import com.example.trail.view.dashboard.DashboardActivity;
import com.example.trail.view.profile.ProfileActivity;
import com.example.trail.view.timeline.TimelineFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapActivity extends BaseActivity<ActivityMapBinding, MapViewModel> implements OnMapReadyCallback, TimelineFragment.TimelineClickListener {

    public static final String TAG = "MapActivity";

    MapViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityMapBinding binding;
    private GoogleMap googleMap;

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
        viewModel.getIntentActionLiveData().observe(this, action -> {
            Intent intent;
            switch (action) {
                // TODO
                case "goBack":
                    this.finish();
                    break;
                case "SettingsActivity":
                case "DashboardActivity":
                    intent = new Intent(this, DashboardActivity.class);
//                    intent.putExtra(EXTRA_TRAIL_ID, binding.);  // todo journeysId
                    startActivity(intent);
                    break;
            }
        });
    }


    // Get a handle to the fragment and register the callback.
    public void initMap() {
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .mapToolbarEnabled(true)
                .zoomControlsEnabled(true)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
//        googleMap.setMapStyle(GoogleMap.MAP_TYPE_SATELLITE)
    }

    @Override
    public void onItemClick(String item) {
        // TODO
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
