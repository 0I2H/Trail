package com.example.trail.view.record;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.model.message.MessageDTO;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.utils.Utils;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RecordViewModel extends BaseViewModel {

    public static final String TAG = "RecordViewModel";

    private final MutableLiveData<Boolean> saveClicked;
    private final MutableLiveData<MessageDTO> uploadResultLiveData;

    @Inject
    public RecordViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.saveClicked = new MutableLiveData<>(false);
        this.uploadResultLiveData = new MutableLiveData<>();
    }

    public void onSaveClicked() {
        saveClicked.setValue(true);
    }

    public void uploadRecord(PinDTO pinDTO) {
        try {
            getCompositeDisposable()
                    .add(getRetrofitService().updatePlace(Utils.jsonConverter(pinDTO))
                            .subscribeOn(getNetworkHelper().getSchedulerIo())
                            .observeOn(getNetworkHelper().getSchedulerUi())
                            .subscribe(uploadResultLiveData::setValue, throwable -> Log.e(TAG, throwable.getMessage())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public MutableLiveData<Boolean> getSaveClicked() {
        return saveClicked;
    }

    public MutableLiveData<MessageDTO> getUploadResultLiveData() {
        return uploadResultLiveData;
    }
}
