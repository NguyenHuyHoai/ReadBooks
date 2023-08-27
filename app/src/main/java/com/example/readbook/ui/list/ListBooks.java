package com.example.readbook.ui.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.readbook.R;
import com.example.readbook.databinding.ActivityListBooksBinding;
import com.example.readbook.ui.library.LibraryAdapter;
import com.example.readbook.ui.library.LibraryMV;
import com.example.readbook.ui.list.filter.Filter;
import com.example.readbook.ui.list.filter.FilterViewModel;
import com.example.readbook.ui.main.MainActivity;

public class ListBooks extends AppCompatActivity {

    private ActivityListBooksBinding binding;
    private ListBooksViewModel listBooksViewModel;
    private ListBooksAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listBooksViewModel = new ViewModelProvider(this).get(ListBooksViewModel.class);
        adapter = new ListBooksAdapter();

        RecyclerView recyclerView = binding.rcvListbooks;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        listBooksViewModel.getBooksLiveData().observe(this, booksList -> {
            adapter.setBooksList(booksList);
            adapter.notifyDataSetChanged();
        });
        listBooksViewModel.loadBooksBySortOption(R.id.radioTrending);
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilter();
            }
        });
        binding.btnBack.setOnClickListener(view -> backHome());
    }

    private void backHome() {
        Intent intent = new Intent(ListBooks.this, MainActivity.class);
        startActivity(intent);
    }

    private void showFilter() {
        Filter bottomSheetFragment = new Filter();

        // Truyền giá trị currentSortType cho Filter
        bottomSheetFragment.setCheckedSortType(listBooksViewModel.getCurrentSortType());

        bottomSheetFragment.setListBooksViewModel(listBooksViewModel);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

}