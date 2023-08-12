package com.example.readbook.ui.bookmarks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentBookmarksBinding;
import com.example.readbook.models.Book;

public class BookmarksFragment extends Fragment {

    private BookmarksMV bookmarksViewModel;
    private FragmentBookmarksBinding binding;
    private BookmarksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookmarksViewModel = new ViewModelProvider(this).get(BookmarksMV.class);
        adapter = new BookmarksAdapter();

        RecyclerView recyclerView = binding.bookFollowList;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        bookmarksViewModel.getBookmarks().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooksList(books);
            adapter.notifyDataSetChanged();
        });

        // Set up click listener
        adapter.setOnBookClickListener(book -> {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle bundle = new Bundle();
            bundle.putString("booksId", book.getBooksId());
            controller.navigate(R.id.action_bookmarks_fragment_to_detail_fragment, bundle);
        });

        // Set up swipe listener
        adapter.setOnBookSwipeListener(position -> {
            // Handle book swipe event
            Book book = adapter.getBookAtPosition(position);
            // Remove the book at the swiped position from the user's followedBooks list in Firestore
            bookmarksViewModel.removeFromFollowedBooks(book.booksId);
            // Remove the book from the adapter
            adapter.removeBookAtPosition(position);
        });

        // Set up ItemTouchHelper for swipe-to-delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Handle book swipe event
                if (adapter != null) {
                    // Handle book swipe event
                    Book book = adapter.getBookAtPosition(position);
                    // Remove the book at the swiped position from the user's followedBooks list in Firestore
                    bookmarksViewModel.removeFromFollowedBooks(book.booksId);
                    // Remove the book from the adapter
                    adapter.removeBookAtPosition(position);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}