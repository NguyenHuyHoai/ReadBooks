package com.example.readbook.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.readbook.models.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<List<Book>> libraryBooksLiveData = new MutableLiveData<>();

    public LiveData<List<Book>> getLibraryBooks() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firebaseFirestore.collection("HistoryBooks")
                    .whereEqualTo("userId", userId)
                    .orderBy("timeView", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<Book> booksList = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String bookId = document.getString("booksId");
                            // Fetch book details from the "Books" collection
                            firebaseFirestore.collection("Books").document(bookId)
                                    .get()
                                    .addOnSuccessListener(bookDocument -> {
                                        Book book = bookDocument.toObject(Book.class);
                                        if (book != null) {
                                            booksList.add(book);
                                            libraryBooksLiveData.setValue(booksList);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }

        return libraryBooksLiveData;
    }
}
