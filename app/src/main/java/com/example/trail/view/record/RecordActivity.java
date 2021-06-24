package com.example.trail.view.record;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityRecordBinding;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.network.helper.NetworkHelper;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.trail.constants.AppConstants.EXTRA_PIN_DTO;

@AndroidEntryPoint
public class RecordActivity extends BaseActivity<ActivityRecordBinding, RecordViewModel> {

    public static final String TAG = "RecordActivity";

    RecordViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityRecordBinding binding;

    private PinDTO pinDTO;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public RecordViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this,
                new SavedStateViewModelFactory(this.getApplication(),
                this))
                .get(RecordViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        pinDTO = (PinDTO) getIntent().getSerializableExtra(EXTRA_PIN_DTO);

        observeViewModel();
    }

    @Override
    public void observeViewModel() {
        viewModel.getSaveClicked().observe(this, state -> {
            if (state) {
                viewModel.getSaveClicked().setValue(false);
                packPinData();
                viewModel.uploadRecord(pinDTO);
            }
        });
    }

    private void packPinData() {
//        pinDTO.setPlaceName(binding.);
//        ...
        // todo ...
    }
}
