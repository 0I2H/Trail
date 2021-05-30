package com.example.trail.view.signup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.model.user.UserDTO;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends BaseViewModel {

    public static final String TAG = "SignUpViewModel";

    private final MutableLiveData<UserDTO> signUpLiveData;

    @Inject
    public SignUpViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.signUpLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<UserDTO> getSignUpLiveData() {
        return signUpLiveData;
    }


}
