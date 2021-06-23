package com.example.trail.view.signup;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.model.user.UserDTO;
import com.example.trail.network.helper.NetworkHelper;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends BaseViewModel {

    public static final String TAG = "SignUpViewModel";

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private final MutableLiveData<UserDTO> signUpLiveData;

    @Inject
    public SignUpViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.signUpLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<UserDTO> getSignUpLiveData() {
        return signUpLiveData;
    }

    public void requestSignUp(UserDTO userDTO) {
        try {
            getCompositeDisposable()
                    .add(getRetrofitService().signupUser("", "", "", "", "")
                    .subscribeOn(getNetworkHelper().getSchedulerIo())
                    .observeOn(getNetworkHelper().getSchedulerUi())
                    .subscribe(join -> {
                        Log.i(TAG, String.valueOf(join.message));
//                        loginLiveData.setValue(join);
                    }, throwable -> Log.e(TAG, throwable.getMessage())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
