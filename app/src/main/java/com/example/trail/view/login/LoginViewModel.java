package com.example.trail.view.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.model.login.LoginDTO;
import com.example.trail.model.user.UserDTO;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends BaseViewModel {

    public static final String TAG = "LoginViewModel";

    private final MutableLiveData<Boolean> loginClicked, signUpClicked, findPasswordClicked;
    private final MutableLiveData<LoginDTO> loginLiveData;
    private final MutableLiveData<UserDTO> userAuthLiveData;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    @Inject
    public LoginViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.loginClicked = new MutableLiveData<>(false);
        this.signUpClicked = new MutableLiveData<>(false);
        this.findPasswordClicked = new MutableLiveData<>(false);
        this.loginLiveData = new MutableLiveData<>();
        this.userAuthLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLoginClicked() {
        return loginClicked;
    }

    public MutableLiveData<LoginDTO> getLoginLiveData() {
        return loginLiveData;
    }

    public MutableLiveData<UserDTO> getUserAuthLiveData() {
        return userAuthLiveData;
    }

    public MutableLiveData<Boolean> getSignUpClicked() {
        return signUpClicked;
    }

    public MutableLiveData<Boolean> getFindPasswordClicked() {
        return findPasswordClicked;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onLoginClick() {
        loginClicked.setValue(true);
    }

    public void onSignUpClick() {
        signUpClicked.setValue(true);
    }

    public void onChangePasswordClick() {
        findPasswordClicked.setValue(true);
    }

    public void requestLogin(LoginDTO loginDTO) {
        try {
            getCompositeDisposable().add(getRetrofitService().loginUser(loginDTO.getEmail(), loginDTO.getPassword())
                    .subscribeOn(getNetworkHelper().getSchedulerIo())
                    .observeOn(getNetworkHelper().getSchedulerUi())
                    .subscribe(login -> {
                        loginLiveData.setValue(login);
                        if(login.isLogin) {         // if login was successful,
                            requestUserAuth();      // get user info (userAuthLiveData)
                            loginClicked.setValue(false);
                        }
                    }, throwable -> Log.e(TAG, throwable.getMessage())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void requestUserAuth() {
        getCompositeDisposable().add(getRetrofitService().authUser()
                .subscribeOn(getNetworkHelper().getSchedulerIo())
                .observeOn(getNetworkHelper().getSchedulerUi())
                .subscribe(userDTO -> {
                    if(userDTO.isAuth) {
                        // Update userAuth data to device
                        AppPreferencesHelper preferencesHelper = new AppPreferencesHelper(context);
                        preferencesHelper.setUserAuth(userDTO);

                        userAuthLiveData.setValue(userDTO);     // todo unnessassary?
                    } else
                        Log.i(TAG, "ERROR: Unable to retrieve userAuth data");
                }, throwable -> Log.e(TAG, throwable.getMessage())));
    }

    public void setLoginClicked(boolean state) {
        loginClicked.setValue(state);
    }


}
