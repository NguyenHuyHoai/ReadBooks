package com.example.readbook.ui.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentExploreBinding;
import com.example.readbook.models.Book;
import com.example.readbook.ui.chapter.Chapters;
import com.example.readbook.ui.explore.banner.BannerAdapter;
import com.example.readbook.ui.list.ListBooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ExploreFragment extends Fragment {

    private ExploreMV exploreViewModel;
    private FragmentExploreBinding binding;
    private BannerAdapter bannerAdapter;
    private Timer timer;

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
        exploreViewModel.setBannerBooks();

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
        exploreViewModel.getBannerBooksLiveData().observe(getViewLifecycleOwner(),this::setUpBannerBookAdapter);
        binding.btnSearchHome.setOnClickListener(v -> navigateToSearch());
        binding.btnFilterlistHome.setOnClickListener(view1 -> navigateToFilter());
    }

    private void navigateToFilter() {
        requireActivity().startActivity(new Intent(requireActivity(), ListBooks.class));
    }

    private void setUpBannerBookAdapter(List<Book> bannerBooks) {
        BannerAdapter bannerAdapter = new BannerAdapter(bannerBooks, new BannerAdapter.OnBookItemClickListener() {
            @Override
            public void onBookItemClick(Book bookList) {
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                Bundle bundle = new Bundle();
                bundle.putString("booksId", bookList.getBooksId());
                controller.navigate(R.id.action_explore_fragment_to_detail_fragment, bundle);
            }
        });
        binding.viewpager.setAdapter(bannerAdapter);
        binding.circleIndicator.setViewPager(binding.viewpager);
        if(bannerBooks == null || bannerBooks.isEmpty() || binding.viewpager == null){
            return;
        }
        // Init timer
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = binding.viewpager.getCurrentItem();
                        int totalItem = bannerBooks.size() - 1;
                        if (currentItem < totalItem){
                            currentItem++;
                            binding.viewpager.setCurrentItem(currentItem);
                        } else {
                            binding.viewpager.setCurrentItem(0);
                        }
                    }
                });
            }
        },500,3000);
    }

    private void navigateToSearch() {
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        controller.navigate(R.id.action_explore_fragment_to_search_fragment);
    }
    private void setUpRecentBooksAdapter(List<Book> recentBooks) {
        BookAdapter bookAdapter = new BookAdapter(recentBooks, new BookAdapter.OnBookItemClickListener() {
            @Override
            public void onBookItemClick(Book book) {
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                Bundle bundle = new Bundle();
                bundle.putString("booksId", book.getBooksId());
                controller.navigate(R.id.action_explore_fragment_to_detail_fragment, bundle);
            }
        });
        binding.rcvMoiNhat.setAdapter(bookAdapter);
    }
    private void setUpTrendingBooksAdapter(List<Book> trendingBooks) {
        BookAdapter bookAdapter = new BookAdapter(trendingBooks, new BookAdapter.OnBookItemClickListener() {
            @Override
            public void onBookItemClick(Book book) {
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                Bundle bundle = new Bundle();
                bundle.putString("booksId", book.getBooksId());
                controller.navigate(R.id.action_explore_fragment_to_detail_fragment, bundle);
            }
        });
        binding.rcvThinhHanh.setAdapter(bookAdapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
}