package com.example.readbook.ui.library;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentLibraryBinding;


public class LibraryFragment extends Fragment {

    private LibraryMV libraryViewModel;
    private FragmentLibraryBinding binding;
    private LibraryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        libraryViewModel = new ViewModelProvider(this).get(LibraryMV.class);
        adapter = new LibraryAdapter();

        RecyclerView recyclerView = binding.bookHistoryList;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        libraryViewModel.getLibraryBooks().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooksList(books);
            adapter.notifyDataSetChanged();
        });
        adapter.setOnBookClickListener(book -> {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle bundle = new Bundle();
            bundle.putString("booksId", book.getBooksId());
            controller.navigate(R.id.action_library_fragment_to_detail_fragment, bundle);
        });
    }
}