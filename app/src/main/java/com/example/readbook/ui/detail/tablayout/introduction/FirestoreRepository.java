package com.example.readbook.ui.detail.tablayout.introduction;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreRepository {

    private FirebaseFirestore db;

    public FirestoreRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getBookData(String booksId, OnBookDataLoadedListener listener) {
        DocumentReference docRef = db.collection("Books").document(booksId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Number countViewLong = documentSnapshot.getLong("viewsCount");
                int countViewValue = countViewLong != null ? countViewLong.intValue() : 0;
                Number ratingDouble = documentSnapshot.getDouble("rating");
                float ratingValue = ratingDouble != null ? ratingDouble.floatValue() : 0.0f;
                Number countRatingLong = documentSnapshot.getLong("ratingsCount");
                int countRatingValue = countRatingLong != null ? countRatingLong.intValue() : 0;
                String description = documentSnapshot.getString("description");

                Log.e("countViewValue", Integer.toString(countViewValue));

                listener.onBookDataLoaded(countViewValue, ratingValue, countRatingValue, description);
            } else {
                // Handle the case when the document doesn't exist
            }
        }).addOnFailureListener(e -> {
            // Handle any errors during the data retrieval
        });
    }

    public interface OnBookDataLoadedListener {
        void onBookDataLoaded(int countView, float rating, int countRating, String description);
    }
}
