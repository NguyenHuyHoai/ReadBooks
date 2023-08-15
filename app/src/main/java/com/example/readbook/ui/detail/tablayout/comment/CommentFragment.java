package com.example.readbook.ui.detail.tablayout.comment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentCommentBinding;
import com.example.readbook.ui.browse.login.Login;


public class CommentFragment extends Fragment {

    private CommentViewModel viewModel;
    private FragmentCommentBinding binding;
    private String booksId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy booksId từ bundle hoặc getArguments()
        // Lấy booksId từ Bundle
        if (getArguments() != null) {
            booksId = getArguments().getString("booksId");
        }

        CommentAdapter adapter = new CommentAdapter();
        binding.commentRecyclerView.setAdapter(adapter);
        binding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        viewModel.loadComments(booksId);
        viewModel.getCommentListLiveData().observe(getViewLifecycleOwner(), comments -> {
            adapter.setCommentList(comments);
        });

        binding.btnPostComment.setOnClickListener(v -> {
            String commentText = binding.etComment.getText().toString();
            if (!TextUtils.isEmpty(commentText)) {
                viewModel.postComment(booksId, commentText);
                binding.etComment.setText("");
            }
        });

        viewModel.getNavigateToLogin().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate) {
                // Thực hiện chuyển sang LoginActivity ở đây
                Intent intent = new Intent(requireContext(), Login.class);
                startActivity(intent);
            }
        });

    }
}
