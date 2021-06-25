package com.example.trail.view.signup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivitySignupBinding;
import com.example.trail.model.user.UserDTO;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.splash.SplashViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends BaseActivity<ActivitySignupBinding, SignUpViewModel> {

    public static final String TAG = "SignUpActivity";

    SignUpViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivitySignupBinding binding;

    private UserDTO userDTO;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public SignUpViewModel getViewModel() {
        return viewModel;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this))
                .get(SignUpViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        userDTO = new UserDTO();

        observeViewModel();
    }

    @Override
    public void observeViewModel() {

        viewModel.getOnSetPreferencesClicked().observe(this, state -> {
            if(state) {
                viewModel.getOnSetPreferencesClicked().setValue(false);
                openSelectionDialog();
            }
        });

        viewModel.getOnNextBtnClicked().observe(this, state -> {
            if (state) {
                viewModel.getOnNextBtnClicked().setValue(false);
                userDTO.setEmail(binding.setEmail.getText().toString());
                userDTO.setPassword(binding.setPw.getText().toString());
                userDTO.setUserName(binding.setUsername.getText().toString());

                if(!(userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty() || userDTO.getUserName().isEmpty() || userDTO.getJourneyType().isEmpty() || userDTO.getLifeStyle().isEmpty())) {
                    viewModel.requestSignUp(userDTO);
                } else {
                    Toast.makeText(this, "Fill out all the information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getServerResponse().observe(this, response -> {
            if (response.isRegisterSuccess()) {
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openSelectionDialog() {
        List<String> journeyTypeList = Arrays.asList(this.getResources().getStringArray(R.array.journey_type));
        List<String> lifeStyleList = Arrays.asList(this.getResources().getStringArray(R.array.lifestyle));

        AlertDialog.Builder lifestyleDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
        lifestyleDialog.setTitle(R.string.dialog_title_lifestyle)
//                .setMessage("Tell us about your lifestyle!")
                .setItems(R.array.lifestyle, (dialog, which) -> {
                    userDTO.setLifeStyle(lifeStyleList.get(which));
                })
//                .setPositiveButton(R.string.ok, (dialog, index) -> {
//                    userDTO.setJourneyType(lifeStyleList.get(index));     // fixme 중복 -> 삭제
//                })
                .setNegativeButton(R.string.cancel, (arg0, arg1) -> {
                    userDTO.setJourneyType("None");
                });

        AlertDialog.Builder journeyTypeDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
        journeyTypeDialog.setTitle(R.string.dialog_title_journey_type)
//                .setMessage("Tell us about how you travel!")
                .setItems(R.array.journey_type, (dialog, which) -> {
                    userDTO.setJourneyType(journeyTypeList.get(which));
                    lifestyleDialog.create().show();
                })
//                .setPositiveButton(R.string.ok, (dialog, index) -> {
//                    lifestyleDialog.create().show();
//                })
                .setNegativeButton(R.string.cancel, (arg0, arg1) -> {
                    userDTO.setJourneyType("None");
                })
                .create().show();
    }



}
