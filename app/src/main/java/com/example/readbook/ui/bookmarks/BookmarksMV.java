package com.example.readbook.ui.bookmarks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookmarksMV extends ViewModel {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private BookmarksRepository repository = new BookmarksRepository();

    public LiveData<List<Book>> getBookmarks() {
        return repository.getBookmarks();
    }

    public void removeFromFollowedBooks(String bookId) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("BookmarksMV", "Removing bookId: " + bookId + " for userId: " + userId);

            firebaseFirestore.collection("Users")
                    .document(userId)
                    .update("followedBooks", FieldValue.arrayRemove(bookId))
                    .addOnSuccessListener(aVoid -> Log.d("BookmarksMV", "Book removed successfully"))
                    .addOnFailureListener(e -> Log.e("BookmarksMV", "Error removing book", e));
        }
    }
}
