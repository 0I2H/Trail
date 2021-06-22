package com.example.trail.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.databinding.ActivityLoginBinding;
import com.example.trail.model.login.LoginDTO;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.main.MainActivity;
import com.example.trail.view.signup.SignUpActivity;
import com.example.trail.view.walkthrough.WalkthroughActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    public static final String TAG = "LoginActivity";

    // DI
    LoginViewModel viewModel;
    @Inject
    NetworkHelper networkHelper;

    private ActivityLoginBinding binding;

    private LoginDTO loginDTO;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this)).get(LoginViewModel.class);
        viewModel.setContext(getApplicationContext());
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        observeViewModel();
    }

    @Override
    public void observeViewModel() {
        viewModel.getLoginClicked().observe(this, state -> {
            if (state) {     // loginBtn clicked
                // check input validity (입력값이 정상값인지 클라이언트 측에서 검사)
                // https://stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression/201378#201378
                String emailValue = binding.emailInput.getText().toString();

                if (Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    loginDTO = new LoginDTO(emailValue, binding.passwordInput.getText().toString());
                    viewModel.requestLogin(loginDTO);
                } else {    // address is invalid
                    viewModel.setLoginClicked(false);
                }
            }
            Toast.makeText(this, "로그인으아아아", Toast.LENGTH_SHORT).show();
        });

        viewModel.getSignUpClicked().observe(this, state -> {
            if (state) {     // signUpBtn clicked
                goToSignUpActivity();
            }
        });

        viewModel.getFindPasswordClicked().observe(this, state -> {
            if (state) {     // loginBtn clicked
                goToFindPasswordActivity();
            }
        });


        viewModel.getLoginLiveData().observe(this, result -> {
            if(result.isLogin()) {        // login successful
                 viewModel.requestUserAuth();
                 goToMainActivity();
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT);
                Log.i(TAG, "Login failed");
            }
        });

        viewModel.getUserAuthLiveData().observe(this, userDTO -> {
            if(!userDTO.isAuth)     // successfully saved data to sharedPreference
                goToMainActivity();
        });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // fixme
    /** If there is a 기록 진행중인 'trail' */
    public void goToDashBoard() {
//     todo   Intent intent = new Intent(this, aldksfslkjsl);
//        startActivity(intent);
    }

    public void showWalkThrough() {
        // todo manifest 추가 아직X
        Intent intent = new Intent(this, WalkthroughActivity.class);
        startActivity(intent);
    }

    public void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToFindPasswordActivity() {
//        Intent intent = new Intent(this, FindPasswordActivity.class);
//        startActivity(intent);
    }
}
