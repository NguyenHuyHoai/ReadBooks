package com.example.readbook.ui.chapter;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Chapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChapterMV extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<DocumentSnapshot> chapterLiveData = new MutableLiveData<>();
    public void getChapter(String booksId, String chaptersName) {
        db.collection("Chapters")
                .whereEqualTo("booksId", booksId)
                .whereEqualTo("chaptersName", chaptersName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
                        chapterLiveData.setValue(task.getResult().getDocuments().get(0));
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public LiveData<DocumentSnapshot> getChapterLiveData() {
        return chapterLiveData;
    }
}
