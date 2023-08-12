package com.example.readbook.ui.bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.readbook.models.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookmarksRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<List<Book>> bookmarksLiveData = new MutableLiveData<>();

    public LiveData<List<Book>> getBookmarks() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firebaseFirestore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<String> followedBooks = (List<String>) documentSnapshot.get("followedBooks");
                            fetchBooksData(followedBooks);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }

        return bookmarksLiveData;
    }

    private void fetchBooksData(List<String> followedBooks) {
        List<Book> bookmarksList = new ArrayList<>();

        firebaseFirestore.collection("Books")
                .whereIn("booksId", followedBooks)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Book book = document.toObject(Book.class);
                        if (book != null) {
                            bookmarksList.add(book);
                        }
                    }
                    bookmarksLiveData.setValue(bookmarksList);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}
