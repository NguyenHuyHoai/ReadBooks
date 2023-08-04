package com.example.readbook.ui.explore;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.readbook.databinding.ItemBookBinding;
import com.example.readbook.models.Book;


import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>{

    private final List<Book> items;
    public interface OnBookItemClickListener {
        void onBookItemClick(Book item);
    }
    private final OnBookItemClickListener listener;

    public BookAdapter(List<Book> items, OnBookItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }
    public List<Book> getItems() {
        return items;
    }

    @NonNull
    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.MyViewHolder holder, int position) {
        Book item = items.get(position);
        Glide.with(holder.binding.novelCover.getContext())
                .load(item.getImageBook())
                .into(holder.binding.novelCover);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookBinding binding;

        public MyViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.novelCoverLayout.setOnClickListener(v ->
                    listener.onBookItemClick(items.get(getAdapterPosition())));
        }
    }
}
