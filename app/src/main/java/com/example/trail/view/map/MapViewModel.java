package com.example.trail.view.map;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.R;
import com.example.trail.base.BaseViewModel;
import com.example.trail.model.pin.PinDTO;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends BaseViewModel {

    public static final String TAG = "MapViewModel";

    private MutableLiveData<String> intentActionLiveData;
    private MutableLiveData<List<PinDTO>> pinListLiveData;
    private MutableLiveData<List<MarkerOptions>> markerListLiveData;
    private MutableLiveData<PolylineOptions> polylineOptionsLiveData;


    @Inject
    public MapViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        intentActionLiveData = new MutableLiveData<>();
        pinListLiveData = new MutableLiveData<>();
        markerListLiveData = new MutableLiveData<>();
        polylineOptionsLiveData = new MutableLiveData<>();
    }

    public void requestTrailPath(int journeyId) {
        try {
            getCompositeDisposable()
                    .add(getRetrofitService().getJourneyPins(journeyId)
                            .subscribeOn(getNetworkHelper().getSchedulerIo())
                            .observeOn(getNetworkHelper().getSchedulerUi())
                            .subscribe(pinListLiveData::setValue, throwable -> Log.e(TAG, throwable.getMessage())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void requestTotalPins() {
        try {
            getCompositeDisposable()
                    .add(getRetrofitService().getTotalPins()
                            .subscribeOn(getNetworkHelper().getSchedulerIo())
                            .observeOn(getNetworkHelper().getSchedulerUi())
                            .subscribe(pinList -> {
                                pinListLiveData.setValue(pinList);
                                List<MarkerOptions> markerList = new ArrayList<>();
                                // Instantiates a new Polyline object and adds points to define a rectangle
                                PolylineOptions polylineOptions = new PolylineOptions();
                                for (PinDTO pinDTO : pinList) {
                                    LatLng loc = new LatLng(Double.parseDouble(pinDTO.getLatitude()), Double.parseDouble(pinDTO.getLongitude()));
                                    markerList.add(new MarkerOptions()
                                            .position(loc)
                                            .title(String.valueOf(pinDTO.getId()))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
//                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))))

                                    polylineOptions.add(loc);
                                }
                                markerListLiveData.setValue(markerList);
                                polylineOptionsLiveData.setValue(polylineOptions);
                            }, throwable -> Log.e(TAG, throwable.getMessage())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public MutableLiveData<String> getIntentActionLiveData() {
        return intentActionLiveData;
    }

    public MutableLiveData<List<PinDTO>> getPinListLiveData() {
        return pinListLiveData;
    }

    public MutableLiveData<List<MarkerOptions>> getMarkerListLiveData() {
        return markerListLiveData;
    }

    public MutableLiveData<PolylineOptions> getPolylineOptionsLiveData() {
        return polylineOptionsLiveData;
    }

    public void onLocationStateBtnClicked() {

    }

    public void pinMarkerSelected() {

    }

    public void onBackBtnClicked() {
//        intentActionLiveData.setValue("goBack");
        intentActionLiveData.setValue("DashboardActivity");
    }

    public void onSettingsBtnClicked() {
        intentActionLiveData.setValue("SettingsActivity");
    }
}
