package com.example.readbook.ui.search;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.readbook.R;
import com.example.readbook.databinding.ItemBookSearchBinding;
import com.example.readbook.models.Book;
import com.example.readbook.ui.explore.BookAdapter;

import java.util.List;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private List<Book> bookList;
    private OnSearchItemClickListener listener;
    public SearchAdapter(List<Book> bookList, OnSearchItemClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    public void filterList(List<Book> filterlist) {
        bookList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBookSearchBinding binding = ItemBookSearchBinding.inflate(layoutInflater, parent, false);
        return new SearchAdapter.SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {

        Book booksItem = bookList.get(position);
        holder.bind(booksItem);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public interface OnSearchItemClickListener {
        void OnSearchItemClick(Book item);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private ItemBookSearchBinding binding;
        public SearchViewHolder(ItemBookSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.itemBookSearch.setOnClickListener(v ->
                    listener.OnSearchItemClick(bookList.get(getAdapterPosition())));
        }

        public void bind(Book book) {
            binding.tvBookName.setText(book.getTitle());
            binding.tvAuthor.setText(book.getAuthor());
            Glide.with(itemView.getContext())
                    .load(book.getImageBook())
                    .placeholder(R.drawable.icon_load)
                    .error(R.drawable.icon_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageBook);
            List<String> genresList = book.getGenres();
            String genresString = TextUtils.join(", ", genresList);
            binding.tvGenres.setText(genresString);
        }
    }
}
