package com.example.readbook.ui.explore;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Book;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExploreMV extends ViewModel {
    private MutableLiveData<List<Book>> recentBooks;
    private MutableLiveData<List<Book>> trendingBooks;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference booksRef =  db.collection("Books");
    public LiveData<List<Book>> getRecentBooksLiveData() {
        if (recentBooks == null) {
            recentBooks = new MutableLiveData<>();
        }
        return recentBooks;
    }

    public LiveData<List<Book>> getTrendingBooksLiveData() {
        if (trendingBooks == null) {
            trendingBooks = new MutableLiveData<>();
        }
        return trendingBooks;
    }
    public void setRecentBooks() {
        booksRef.orderBy("creationTimestamp", Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Book> recentBooksList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Book book = document.toObject(Book.class);
                    recentBooksList.add(book);
                }
                recentBooks.setValue(recentBooksList);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public void setTrendingBooks() {
            booksRef.orderBy("viewsCount", Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Book> trendingBooksList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Book book = document.toObject(Book.class);
                    trendingBooksList.add(book);
                    for(Book book1: trendingBooksList){
                    }
                }
                trendingBooks.setValue(trendingBooksList);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
