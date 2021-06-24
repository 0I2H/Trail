package com.example.trail.utils;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trail.model.pin.PinDTO;
import com.example.trail.view.map.PinListAdapter;


import java.util.List;

public final class BindingUtils {

    // ViewModel의 data를 Adapter에 전달하여 함께 사용
    @BindingAdapter({"pin_adapter"})
    public static void addPinList(RecyclerView recyclerView, List<PinDTO> pinList) {
        PinListAdapter pinListAdapter = (PinListAdapter) recyclerView.getAdapter();
        if (pinListAdapter != null) {
            pinListAdapter.clearItems();
            pinListAdapter.addItems(pinList);
        }
    }
}
