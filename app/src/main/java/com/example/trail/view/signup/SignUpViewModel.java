package com.example.trail.view.signup;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.model.message.MessageDTO;
import com.example.trail.model.user.UserDTO;
import com.example.trail.network.helper.NetworkHelper;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends BaseViewModel {

    public static final String TAG = "SignUpViewModel";


    private MutableLiveData<MessageDTO> serverResponseLiveData;
    private MutableLiveData<Boolean> onNextBtnClicked, onSetProfilePhotoClicked, onSetPreferencesClicked;

    @Inject
    public SignUpViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.serverResponseLiveData = new MutableLiveData<>();
        this.onNextBtnClicked = new MutableLiveData<>(false);
        this.onSetProfilePhotoClicked = new MutableLiveData<>(false);
        this.onSetPreferencesClicked = new MutableLiveData<>(false);
    }

    public MutableLiveData<MessageDTO> getServerResponse() {
        return serverResponseLiveData;
    }

    public MutableLiveData<Boolean> getOnNextBtnClicked() {
        return onNextBtnClicked;
    }

    public MutableLiveData<Boolean> getOnSetProfilePhotoClicked() {
        return onSetProfilePhotoClicked;
    }

    public MutableLiveData<Boolean> getOnSetPreferencesClicked() {
        return onSetPreferencesClicked;
    }

    public void onNextBtnClicked() {
        onNextBtnClicked.setValue(true);
    }

    public void setProfilePhoto() {

    }

    public void setPreferences() {
        onSetPreferencesClicked.setValue(true);
    }

    public void requestSignUp(UserDTO userDTO) {
        try {
            getCompositeDisposable()
                    .add(getRetrofitService().signupUser(userDTO.getEmail(), userDTO.getPassword(), userDTO.getUserName(), userDTO.getJourneyType(), userDTO.getLifeStyle())
                    .subscribeOn(getNetworkHelper().getSchedulerIo())
                    .observeOn(getNetworkHelper().getSchedulerUi())
                    .subscribe(serverResponseLiveData::setValue, throwable -> Log.e(TAG, throwable.getMessage())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
