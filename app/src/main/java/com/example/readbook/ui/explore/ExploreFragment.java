package com.example.readbook.ui.explore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentExploreBinding;
import com.example.readbook.models.Book;
import com.example.readbook.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExploreFragment extends Fragment {

    private ExploreMV exploreViewModel;
    private FragmentExploreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up ViewModel
        exploreViewModel = new ViewModelProvider(this).get(ExploreMV.class);
        exploreViewModel.setRecentBooks();
        exploreViewModel.setTrendingBooks();

        // Set up RecyclerViews
        int spaceInDp = 10;
        int spaceInPixels = (int) (spaceInDp * getResources().getDisplayMetrics().density);
        binding.rcvMoiNhat.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.rcvMoiNhat.addItemDecoration(new SpaceItemDecoration(spaceInPixels));

        binding.rcvThinhHanh.setLayoutManager(new GridLayoutManager(requireContext(), 2,
                GridLayoutManager.VERTICAL, false));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_between_items);
        binding.rcvThinhHanh.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        // Set up Adapters
        exploreViewModel.getRecentBooksLiveData().observe(getViewLifecycleOwner(), this::setUpRecentBooksAdapter);
        exploreViewModel.getTrendingBooksLiveData().observe(getViewLifecycleOwner(), this::setUpTrendingBooksAdapter);
    }

    private void setUpRecentBooksAdapter(List<Book> recentBooks) {
        BookAdapter bookAdapter = new BookAdapter(recentBooks, new BookAdapter.OnBookItemClickListener() {
            @Override
            public void onBookItemClick(Book book) {
                // Xử lý sự kiện khi người dùng click vào cuốn sách
                // Ví dụ: Chuyển đến màn hình chi tiết sách với thông tin của cuốn sách được click
            }
        });
        binding.rcvMoiNhat.setAdapter(bookAdapter);
    }
    private void setUpTrendingBooksAdapter(List<Book> trendingBooks) {
        BookAdapter bookAdapter = new BookAdapter(trendingBooks, new BookAdapter.OnBookItemClickListener() {
            @Override
            public void onBookItemClick(Book book) {
                // Xử lý sự kiện khi người dùng click vào cuốn sách
                // Ví dụ: Chuyển đến màn hình chi tiết sách với thông tin của cuốn sách được click
            }
        });
        binding.rcvThinhHanh.setAdapter(bookAdapter);
    }

}