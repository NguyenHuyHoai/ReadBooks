package com.example.readbook.ui.detail.tablayout.chapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Chapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChapterListViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Chapter>> chapterListLiveData = new MutableLiveData<>();

    public LiveData<List<Chapter>> getChapterListLiveData() {
        return chapterListLiveData;
    }

    public void loadChapters(String booksId) {
        db.collection("Chapters")
                .whereEqualTo("booksId", booksId)
                .orderBy("chaptersName", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Chapter> chapters = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Chapter chapter = documentSnapshot.toObject(Chapter.class);
                        chapters.add(chapter);
                    }
                    chapterListLiveData.setValue(chapters);
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });
    }

}

