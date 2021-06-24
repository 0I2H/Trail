package com.example.trail.view.signup.fragment;

import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FragmentViewModel extends BaseViewModel {

    @Inject
    public FragmentViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    public void goNextStep(int currentStep) {

    }

    public void setPreferences() {
        // PreferenceFragment
    }

    public void setProfilePhoto() {

    }

    public void inflateStep(int step) {

    }
}
