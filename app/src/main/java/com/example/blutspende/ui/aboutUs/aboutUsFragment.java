package com.example.blutspende.ui.aboutUs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blutspende.R;
import com.example.blutspende.databinding.FragmentAboutUsBinding;
import com.example.blutspende.databinding.FragmentBeADonorBinding;

public class aboutUsFragment extends Fragment {

    private FragmentAboutUsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}