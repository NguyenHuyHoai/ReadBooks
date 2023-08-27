package com.example.readbook.ui.list.filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentFilterBinding;
import com.example.readbook.ui.list.ListBooksViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;


public class Filter extends BottomSheetDialogFragment {

    private FragmentFilterBinding binding;
    private FilterViewModel viewModel;
    private ListBooksViewModel listBooksViewModel;
    public void setListBooksViewModel(ListBooksViewModel viewModel) {
        listBooksViewModel = viewModel;
    }

    private int checkedSortType = R.id.radioTrending; // Mặc định là thịnh hành

    public void setCheckedSortType(int sortType) {
        checkedSortType = sortType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        viewModel.getGenresListLiveData().observe(getViewLifecycleOwner(), genresList -> {
            // Tạo các Checkbox động trong FlowLayout
            for (String genre : genresList) {
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setText(genre);

                // Đặt các thuộc tính cho CheckBox
                checkBox.setTextColor(getResources().getColorStateList(R.color.radio_button_selected_text_color));
                checkBox.setButtonDrawable(R.drawable.radio_button_selected_background);
                checkBox.setTextSize(16);
                checkBox.setPadding(30, 30, 30, 30);// Kích thước chữ

                binding.flowLayout.addView(checkBox);
            }
        });

        // Đặt RadioButton được chọn tương ứng với giá trị checkedSortType
        if (checkedSortType == R.id.radioTrending) {
            binding.radioTrending.setChecked(true);
        } else if (checkedSortType == R.id.radioRecent) {
            binding.radioRecent.setChecked(true);
        } else if (checkedSortType == R.id.radioRanking) {
            binding.radioRanking.setChecked(true);
        }

        viewModel.loadGenresList();

//        binding.radioGroupSort.setOnCheckedChangeListener((group, checkedId) -> {
//            listBooksViewModel.loadBooksBySortOption(checkedId);
//        });

        binding.btnFilter.setOnClickListener(view1 -> selectFilter());
    }

    private void selectFilter() {
        int checkedRadioButtonId = binding.radioGroupSort.getCheckedRadioButtonId();
        if (listBooksViewModel != null) {
            listBooksViewModel.loadBooksBySortOption(checkedRadioButtonId);
        }
        dismiss();
    }

}