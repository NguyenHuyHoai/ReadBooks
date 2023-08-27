package com.example.readbook.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentSearchBinding;
import com.example.readbook.models.Book;

import java.util.ArrayList;
import java.util.List;


public class Search extends Fragment implements SearchAdapter.OnSearchItemClickListener{
    private List<Book> bookslist;
    private FragmentSearchBinding binding;
    private SearchMV searchViewModel;
    private SearchAdapter searchAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchMV.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        bookslist = new ArrayList<>();
        binding.bookSearchList.setLayoutManager(new LinearLayoutManager(getContext()));

        buildRecyclerView();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();
            }
        });
        return binding.getRoot();
    }

    private void buildRecyclerView() {
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), bookslist -> {
            searchAdapter = new SearchAdapter(bookslist, this);
            binding.bookSearchList.setAdapter(searchAdapter);
            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    List<Book> filteredlist = new ArrayList<>();
                    String lowerCaseQuery = newText.toLowerCase();
                    for (Book item : bookslist) {
                        if (item.getTitle().toLowerCase().contains(newText.toLowerCase())||
                                item.getAuthor().toLowerCase().contains(newText.toLowerCase()) ||
                                containsGenre(item.getGenres(), lowerCaseQuery)) {
                            filteredlist.add(item);
                        }
                    }
                    if (filteredlist.isEmpty()) {
                        searchAdapter.filterList(filteredlist);
                    } else {
                        searchAdapter.filterList(filteredlist);
                    }
                    return false;
                }
                private boolean containsGenre(List<String> genres, String query) {
                    for (String genre : genres) {
                        if (genre.toLowerCase().contains(query)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        });
    }



    @Override
    public void OnSearchItemClick(Book book) {
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle bundle = new Bundle();
        bundle.putString("booksId", book.getBooksId());
        controller.navigate(R.id.action_search_fragment_to_detail_fragment, bundle);
    }
}