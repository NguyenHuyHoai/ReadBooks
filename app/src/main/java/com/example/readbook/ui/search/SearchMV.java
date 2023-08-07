package com.example.readbook.ui.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Book;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class SearchMV extends ViewModel {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();

    public LiveData<List<Book>> getSearchResults() {
        return searchResults;
    }

    public SearchMV() {
        searchBooks();
    }

    public void searchBooks() {
        db.collection("Books")
                .orderBy("title", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Book> books = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        books.add(book);
                    }
                    searchResults.postValue(books);
                })
                .addOnFailureListener(e -> {
                    Log.e("LỖI RỒI", "LOOOOOOOO");
                });
    }
}
