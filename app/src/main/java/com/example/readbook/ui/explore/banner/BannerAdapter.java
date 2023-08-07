package com.example.readbook.ui.explore.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.readbook.R;
import com.example.readbook.databinding.ItemBannerBinding;
import com.example.readbook.models.Book;
import com.example.readbook.ui.explore.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private List<Book> bookList;
    private Context context;

    public interface OnBookItemClickListener {
        void onBookItemClick(Book bookList);
    }
    private OnBookItemClickListener listener;

    public BannerAdapter(List<Book> bookList, OnBookItemClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemBannerBinding binding = ItemBannerBinding.inflate(inflater, container, false);

        Book book = bookList.get(position);
        ImageView imageView = binding.imgBanner;
        Glide.with(container.getContext())
                .load(book.getCoverImage())
                .placeholder(R.drawable.icon_error)
                .into(imageView);

        container.addView(binding.getRoot());

        // Gắn sự kiện click vào banner
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookItemClick(book);
            }
        });
        return binding.getRoot();
    }
    @Override
    public int getCount() {
        if (bookList != null){
            return bookList.size();
        }
        return 0;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
