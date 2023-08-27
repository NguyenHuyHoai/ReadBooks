package com.example.readbook.ui.list.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.R;
import com.example.readbook.models.Book;
import com.example.readbook.models.Genre;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FilterViewModel extends ViewModel {
    private MutableLiveData<List<String>> genresListLiveData = new MutableLiveData<>();

    public LiveData<List<String>> getGenresListLiveData() {
        return genresListLiveData;
    }

    public void loadGenresList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Genres")
                .orderBy("genresName") // Sắp xếp theo trường "genresName"
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> genresList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String genreName = document.getString("genresName");
                        if (genreName != null) {
                            genresList.add(genreName);
                        }
                    }
                    genresListLiveData.setValue(genresList);
                });
    }

}