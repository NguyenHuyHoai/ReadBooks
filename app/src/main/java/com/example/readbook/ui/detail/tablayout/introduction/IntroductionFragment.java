package com.example.readbook.ui.detail.tablayout.introduction;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentChapterListBinding;
import com.example.readbook.databinding.FragmentIntroductionBinding;
import com.example.readbook.ui.browse.login.Login;
import com.example.readbook.ui.detail.tablayout.chapter.ChapterListViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class IntroductionFragment extends Fragment {
    private IntroductionViewModel viewModel;
    private FragmentIntroductionBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String booksId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIntroductionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(IntroductionViewModel.class);


        // Lấy booksId từ Bundle
        if (getArguments() != null) {
            booksId = getArguments().getString("booksId");
        }

        viewModel.loadBookData(booksId);
        loadData();

        binding.btnRating.setOnClickListener(v -> showRatingDialog());
    }

    private void showRatingDialog() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore.getInstance()
                    .collection("UserRating")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("booksId", booksId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        final int[] selectedRating;
                        if (querySnapshot.isEmpty()) {
                            selectedRating = new int[]{-1};
                        } else {
                            DocumentSnapshot ratingSnapshot = querySnapshot.getDocuments().get(0);
                            Long userRating = ratingSnapshot.getLong("rating");
                            int defaultRating = userRating != null ? userRating.intValue() : -1;
                            selectedRating = new int[]{defaultRating};
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Chọn đánh giá");

                        String[] ratings = {"1 sao", "2 sao", "3 sao", "4 sao", "5 sao"};
                        int defaultPosition = selectedRating[0] - 1;

                        builder.setSingleChoiceItems(ratings, defaultPosition, (dialog, which) -> {
                            selectedRating[0] = which + 1;
                        });

                        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                            if (selectedRating[0] != -1) {
                                updateRating(selectedRating[0]);
                            }
                            dialog.dismiss();
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    });

        }
        else
        {
            startActivity(new Intent(requireActivity(), Login.class));
        }
    }


    private void updateRating(int selectedRating) {
        viewModel.updateRating(booksId, selectedRating);
    }

    private void loadData() {
        viewModel.getCountView().observe(getViewLifecycleOwner(), countView -> {
            if (countView != null) {
                binding.countView.setText(countView);
            }
        });
        viewModel.getRating().observe(getViewLifecycleOwner(), rating -> {
            if (rating != null) {
                binding.Rating.setText(rating);
            }
        });
        viewModel.getCountRating().observe(getViewLifecycleOwner(), countRating -> {
            if (countRating != null) {
                binding.countRating.setText(countRating);
            }
        });
        viewModel.getDescription().observe(getViewLifecycleOwner(), description -> {
            if (description != null) {
                binding.description.setText(description);
            }
        });
    }

}