package com.example.readbook.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailMV extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<DocumentSnapshot> bookLiveData = new MutableLiveData<>();
    public void getBook(String booksId) {
        db.collection("Books")
                .whereEqualTo("booksId", booksId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        bookLiveData.setValue(task.getResult().getDocuments().get(0));
                    } else {
                        bookLiveData.setValue(null);
                    }
                });
    }

    public LiveData<DocumentSnapshot> getBookLiveData() {
        return bookLiveData;
    }
}
