package com.example.trail.view.timeline;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseFragment;
import com.example.trail.databinding.FragmentTimelineBinding;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.map.MapViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.FragmentScoped;

//@AndroidEntryPoint
public class TimelineFragment extends BaseFragment<FragmentTimelineBinding, TimelineViewModel>  {

//    @Inject
    NetworkHelper networkHelper;

    TimelineViewModel viewModel;

    private FragmentTimelineBinding binding;

    private TimelineClickListener listener;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_timeline;
    }

    @Override
    public TimelineViewModel getViewModel() {
        return viewModel;
    }

    public static TimelineFragment newInstance() {
        Bundle args = new Bundle();
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate()
//    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getActivity().getApplication(), this))
                .get(TimelineViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // todo https://www.simplifiedcoding.net/bottom-sheet-android/
//        dismissAllowingStateLoss();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    class TimelineBottomSheet extends BottomSheetDialogFragment {

    }

    public interface TimelineClickListener {
        void onItemClick(String item);
    }

}
