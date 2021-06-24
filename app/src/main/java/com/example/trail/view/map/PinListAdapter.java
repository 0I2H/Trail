package com.example.trail.view.map;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trail.databinding.ItemTimelinePinBinding;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.view.map.timeline.TimelineFragment;

import java.util.ArrayList;
import java.util.List;

public class PinListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PinDTO> pinList = new ArrayList<>();
    private MapActivity activity;
    private RecyclerView recyclerView;

    private boolean isPedometerType = false;

//    public PinListAdapter() {
//        this.pinList = new ArrayList<>();
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimelinePinBinding binding = ItemTimelinePinBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PinListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PinListViewHolder) {
            ((PinListViewHolder) holder).bind(pinList.get(position));
            holder.itemView.setOnClickListener(v -> {
                activity.goToRecordActivity(pinList.get(position));
            });
        } else
            return;
    }

    @Override
    public int getItemCount() {
        return pinList == null ? 0 : pinList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class PinListViewHolder extends RecyclerView.ViewHolder {

        private ItemTimelinePinBinding binding;

        PinListViewHolder(ItemTimelinePinBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PinDTO pinDTO) {
            // TODO
            binding.pinIcon.setEnabled(pinDTO.getStatus().equals("0"));
            binding.pinTime.setText(pinDTO.getPinTime());
            binding.pinLocation.setText(pinDTO.getPlaceName());
        }
    }

    public void clearItems() {
        pinList.clear();
    }

    public void addItems(List<PinDTO> pinList) {
        if(pinList != null) {
            this.pinList.addAll(pinList);
            notifyDataSetChanged();
        }
    }

    public void setActivity (MapActivity activity) {
        this.activity = activity;
    }
}
