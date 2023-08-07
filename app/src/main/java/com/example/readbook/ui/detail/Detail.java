package com.example.readbook.ui.detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.readbook.R;
import com.example.readbook.databinding.FragmentDetailBinding;
import com.example.readbook.databinding.FragmentExploreBinding;
import com.example.readbook.models.Book;
import com.example.readbook.ui.chapter.Chapters;

import java.util.ArrayList;


public class Detail extends Fragment {
    private DetailMV detailViewModel;
    private FragmentDetailBinding binding;
    private String booksId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        setUpObservers();
        setUpListeners();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();
            }
        });
    }

    private void loadData() {
        if (getArguments() != null) {
            booksId = getArguments().getString("booksId");
        }
        detailViewModel = new ViewModelProvider(requireActivity()).get(DetailMV.class);
    }

    private void setUpObservers() {
        detailViewModel.getBookLiveData().observe(getViewLifecycleOwner(), documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Book book = documentSnapshot.toObject(Book.class);
                if (book != null) {
                    RequestManager requestManager = Glide.with(this);
                    requestManager.load(book.getImageBook()).into(binding.imageBook);
                    binding.tvBookName.setText(book.getTitle());
                    ArrayList<String> genresList = (ArrayList<String>) book.getGenres();
                    String genresString = TextUtils.join(", ", genresList);
                    binding.tvGenres.setText(genresString);
                    binding.tvAuthor.setText(book.getAuthor());
                }
            } else {
                Toast.makeText(getContext(), "Novel not found", Toast.LENGTH_SHORT).show();
            }
        });
        detailViewModel.getBook(booksId);
    }

    private void setUpListeners() {
        binding.btnRead.setOnClickListener(v -> navigateToChapterList());
    }
    private void navigateToChapterList() {
        Bundle bundle = new Bundle();
        bundle.putString("booksId", booksId);
        requireActivity().startActivity(new Intent(requireActivity(), Chapters.class).putExtras(bundle));
    }
}