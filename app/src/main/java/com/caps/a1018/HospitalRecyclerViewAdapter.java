package com.caps.a1018;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caps.a1018.databinding.ItemHospitalBinding;

import java.util.ArrayList;
import java.util.List;

interface OnHospitalPhoneClickListener {
    void onPhotoNumberClickListener(String phoneNumber);
}

public class HospitalRecyclerViewAdapter extends RecyclerView.Adapter<HospitalRecyclerViewAdapter.HospitalViewHolder> {
    private List<Hospital> hospitalList;
    private OnHospitalPhoneClickListener onHospitalPhoneClickListener;

    HospitalRecyclerViewAdapter(OnHospitalPhoneClickListener onHospitalPhoneClickListener) {
        hospitalList = new ArrayList<>();
        this.onHospitalPhoneClickListener = onHospitalPhoneClickListener;
    }

    void submitList(List<Hospital> hospitalList) {
        if (this.hospitalList.size() != 0)
            this.hospitalList.clear();
        this.hospitalList.addAll(hospitalList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HospitalViewHolder(ItemHospitalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), onHospitalPhoneClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.bind(hospital);
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    static class HospitalViewHolder extends RecyclerView.ViewHolder {
        private final ItemHospitalBinding binding;
        private final OnHospitalPhoneClickListener onHospitalPhoneClickListener;

        public HospitalViewHolder(ItemHospitalBinding itemHospitalBinding, OnHospitalPhoneClickListener onHospitalPhoneClickListener) {
            super(itemHospitalBinding.getRoot());
            this.binding = itemHospitalBinding;
            this.onHospitalPhoneClickListener = onHospitalPhoneClickListener;
        }

        public void bind(final Hospital hospital) {
            binding.textView4.setOnClickListener(view -> {
                onHospitalPhoneClickListener.onPhotoNumberClickListener(hospital.getPhone());
            });
            binding.textView.setText(hospital.getName());
            binding.textView2.setText(hospital.getAddress());
            binding.textView3.setText(hospital.getDistance().concat("km"));
            binding.textView4.setText(hospital.getPhone());
        }
    }
}