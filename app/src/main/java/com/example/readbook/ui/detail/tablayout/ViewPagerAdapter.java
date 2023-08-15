package com.example.readbook.ui.detail.tablayout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.readbook.ui.detail.tablayout.chapter.ChapterListFragment;
import com.example.readbook.ui.detail.tablayout.comment.CommentFragment;
import com.example.readbook.ui.detail.tablayout.introduction.IntroductionFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private String booksId; // Để lưu trữ booksId

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String booksId) {
        super(fragmentActivity);
        this.booksId = booksId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                IntroductionFragment introductionFragment = new IntroductionFragment();
                introductionFragment.setArguments(createBundle());
                return introductionFragment;
            case 1:
                CommentFragment commentFragment = new CommentFragment();
                commentFragment.setArguments(createBundle());
                return commentFragment;
            case 2:
                ChapterListFragment chapterListFragment = new ChapterListFragment();
                chapterListFragment.setArguments(createBundle());
                return chapterListFragment;
            default:
                return new IntroductionFragment();
        }
    }

    private Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("booksId", booksId);
        return bundle;
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng các tab/fragment
    }
}
