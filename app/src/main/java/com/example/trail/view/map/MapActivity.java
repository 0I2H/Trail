package com.example.trail.view.map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityMapBinding;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.dashboard.DashboardActivity;
import com.example.trail.view.map.timeline.TimelineFragment;
import com.example.trail.view.record.RecordActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.trail.constants.AppConstants.EXTRA_PIN_DTO;

@AndroidEntryPoint
public class MapActivity extends BaseActivity<ActivityMapBinding, MapViewModel> implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, TimelineFragment.TimelineClickListener {

    public static final String TAG = "MapActivity";

    MapViewModel viewModel;

    private PinListAdapter pinListAdapter;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityMapBinding binding;
    private GoogleMap googleMap;

    TimelineFragment timelineFragment;

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

        initTimelineFragment();

        observeViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int journeyId = appPreferencesHelper.getCurrentTrailId();
        if (journeyId > 0) {
            viewModel.requestTrailPath(appPreferencesHelper.getCurrentTrailId());
        } else {
//            viewModel.requestTrailPath(2);
            viewModel.requestTotalPins();
        }
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

        viewModel.getPinListLiveData().observe(this, pinList -> {
            timelineFragment.getViewModel().getPinListLiveData().setValue(pinList);
        });
//
//        viewModel.getMarkerListLiveData().observe(this, markerOptions -> {
//            // ? todo -> onMapReady()로 옮기는 것이 맞는 듯해 보임
//        });
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

        mapFragment.getMapAsync(this);
    }

    // https://developers.google.com/maps/documentation/android-sdk/marker
    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        viewModel.getMarkerListLiveData().observe(this, markerOptions -> {
            googleMap.clear();
            for(MarkerOptions markerOp : markerOptions) {
                googleMap.addMarker(markerOp);
            }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(markerOptions.get(0).)));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(0));

    });

        viewModel.getPolylineOptionsLiveData().observe(this, polylineOptions -> {
            // Get back the mutable Polyline
            Polyline polyline = googleMap.addPolyline(polylineOptions);

        });
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onItemClick(String item) {
        // TODO
    }

    public void initTimelineFragment() {
        pinListAdapter = new PinListAdapter();
        pinListAdapter.setActivity(this);

        FragmentManager fm = getSupportFragmentManager();
        timelineFragment = TimelineFragment.newInstance(pinListAdapter);
        fm.beginTransaction().add(R.id.timeline_place_holder, timelineFragment).commit();
//
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

    public void goToRecordActivity (PinDTO pinDTO) {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra(EXTRA_PIN_DTO, pinDTO);
        startActivity(intent);
    }
}
