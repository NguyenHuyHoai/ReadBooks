package com.example.readbook.ui.detail.tablayout.chapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readbook.databinding.ItemChapterBinding;
import com.example.readbook.models.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {

    private List<Chapter> chapterList = new ArrayList<>();
    private OnChapterClickListener chapterClickListener;

    public void setChapterList(List<Chapter> chapters) {
        this.chapterList = chapters;
        notifyDataSetChanged();
    }

    public void setOnChapterClickListener(OnChapterClickListener listener) {
        this.chapterClickListener = listener;
    }

    public interface OnChapterClickListener {
        void onChapterClick(Chapter chapter);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChapterBinding binding = ItemChapterBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.bind(chapter);

        holder.itemView.setOnClickListener(v -> {
            if (chapterClickListener != null) {
                chapterClickListener.onChapterClick(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemChapterBinding binding;

        public ViewHolder(@NonNull ItemChapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chapter chapter) {
            binding.tvchapterName.setText(chapter.getChaptersName());
            // Bạn có thể thêm các tác vụ khác ở đây nếu cần
        }
    }
}

