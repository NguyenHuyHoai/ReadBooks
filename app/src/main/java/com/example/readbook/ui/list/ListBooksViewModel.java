package com.example.readbook.ui.list;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.R;
import com.example.readbook.models.Book;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListBooksViewModel extends ViewModel {
    private MutableLiveData<List<Book>> booksLiveData = new MutableLiveData<>();
    private int currentSortType = R.id.radioTrending;
    public LiveData<List<Book>> getBooksLiveData() {
        return booksLiveData;
    }
    public int getCurrentSortType() {
        return currentSortType;
    }

    public void setCurrentSortType(int sortType) {
        currentSortType = sortType;
    }

    public void loadBooksBySortOption(int checkedRadioButtonId) {
        String queryField;
        if (checkedRadioButtonId == R.id.radioTrending) {
            queryField = "viewsCount";
        } else if (checkedRadioButtonId == R.id.radioRecent) {
            queryField = "creationTimestamp";
        } else if (checkedRadioButtonId == R.id.radioRanking) {
            queryField = "rating";
        } else {
            return;
        }
        loadListBooks(queryField);
        setCurrentSortType(checkedRadioButtonId);
    }

    private void loadListBooks(String queryField) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books")
                .orderBy(queryField, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Book> books = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        books.add(book);
                    }
                    booksLiveData.setValue(books);
                });
    }

}
