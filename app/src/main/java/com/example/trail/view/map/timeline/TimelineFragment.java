package com.example.trail.view.map.timeline;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseFragment;
import com.example.trail.databinding.FragmentTimelineBinding;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.map.PinListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TimelineFragment extends BaseFragment<FragmentTimelineBinding, TimelineViewModel>  {

    @Inject
    NetworkHelper networkHelper;

    TimelineViewModel viewModel;

    private FragmentTimelineBinding binding;

    private TimelineClickListener listener;

    static PinListAdapter pinListAdapter;
    Context activityContext;

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

    public static TimelineFragment newInstance(PinListAdapter adapter) {
        pinListAdapter = adapter;

        Bundle args = new Bundle();
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate()
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getActivity().getApplication(), this))
                .get(TimelineViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView(activityContext, pinListAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.activityContext = context;


        // todo https://www.simplifiedcoding.net/bottom-sheet-android/
//        dismissAllowingStateLoss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void initRecyclerView(Context context, PinListAdapter adapter) {
        binding.pinRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.pinRecyclerView.setAdapter(adapter);
    }

    class TimelineBottomSheet extends BottomSheetDialogFragment {

    }

    public interface TimelineClickListener {
        void onItemClick(String item);
    }

    public void goRecordActivity(PinDTO pinDTO) {
        // activity's intent
        // todo
    }
}
