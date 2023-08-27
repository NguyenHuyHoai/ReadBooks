package com.example.readbook.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.readbook.databinding.ItemBookSearchBinding;
import com.example.readbook.models.Book;
import com.example.readbook.ui.bookmarks.BookmarksAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListBooksAdapter extends RecyclerView.Adapter<ListBooksAdapter.ViewHolder>{

    private List<Book> booksList = new ArrayList<>();
    private ListBooksAdapter.OnBookClickListener bookClickListener;

    public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
    }

    public void setOnBookClickListener(ListBooksAdapter.OnBookClickListener listener) {
        this.bookClickListener = listener;
    }
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    @NonNull
    @Override
    public ListBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookSearchBinding binding = ItemBookSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListBooksAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBooksAdapter.ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bind(book);

        holder.itemView.setOnClickListener(v -> {
            if (bookClickListener != null) {
                bookClickListener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemBookSearchBinding binding;

        public ViewHolder(@NonNull ItemBookSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Book book) {
            binding.tvBookName.setText(book.getTitle());
            binding.tvAuthor.setText(book.getAuthor());
            binding.tvGenres.setText(book.getGenres().toString()); // Convert List to String
            Glide.with(itemView.getContext()).load(book.getImageBook()).into(binding.imageBook);
        }
    }
}
