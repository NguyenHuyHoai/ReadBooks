package com.example.readbook.ui.chapter.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentSettingsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SettingsFragment extends BottomSheetDialogFragment {

    private OnSettingsChangeListener onSettingsChangeListener;

    public interface OnSettingsChangeListener {
        void onSettingsChanged(int textSize, int textColorResId, int bgColorResId);
    }

    public void setOnSettingsChangeListener(OnSettingsChangeListener listener) {
        onSettingsChangeListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        binding.radioSmall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioMedium.setChecked(false);
                binding.radioLarge.setChecked(false);
            }
        });

        binding.radioMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioSmall.setChecked(false);
                binding.radioLarge.setChecked(false);
            }
        });

        binding.radioLarge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioSmall.setChecked(false);
                binding.radioMedium.setChecked(false);
            }
        });

        binding.radioDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioLight.setChecked(false);
                binding.radioYellow.setChecked(false);
            }
        });

        binding.radioLight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioDark.setChecked(false);
                binding.radioYellow.setChecked(false);
            }
        });

        binding.radioYellow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioDark.setChecked(false);
                binding.radioLight.setChecked(false);
            }
        });

        binding.btnUpdate.setOnClickListener(view -> {
            if (onSettingsChangeListener != null) {
                int textSize = binding.radioSmall.isChecked() ? 14 :
                        binding.radioMedium.isChecked() ? 16 :
                                binding.radioLarge.isChecked() ? 18 : 16;

                int textColorResId = binding.radioDark.isChecked() ? R.color.black :
                        binding.radioLight.isChecked() ? R.color.white :
                                binding.radioYellow.isChecked() ? R.color.black : R.color.black;

                int bgColorResId = binding.radioDark.isChecked() ? R.color.white :
                        binding.radioLight.isChecked() ? R.color.black :
                                binding.radioYellow.isChecked() ? R.color.yellow : R.color.white;

                onSettingsChangeListener.onSettingsChanged(textSize, textColorResId, bgColorResId);
                dismiss();
            }
        });

        return binding.getRoot();
    }
}