package com.example.readbook.ui.detail.tablayout.introduction;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class IntroductionViewModel extends ViewModel {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<String> countView = new MutableLiveData<>();
    private MutableLiveData<String> rating = new MutableLiveData<>();
    private MutableLiveData<String> countRating = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();

    private FirestoreRepository firestoreRepository = new FirestoreRepository();

    public LiveData<String> getCountView() {
        return countView;
    }

    public LiveData<String> getRating() {
        return rating;
    }

    public LiveData<String> getCountRating() {
        return countRating;
    }

    public LiveData<String> getDescription() {
        return description;
    }

    public void loadBookData(String booksId) {
        firestoreRepository.getBookData(booksId, new FirestoreRepository.OnBookDataLoadedListener() {
            @Override
            public void onBookDataLoaded(int countViewData, float ratingData, int countRatingData, String descriptionData) {
                countView.setValue(String.valueOf(countViewData)); // Chuyển int thành String để set vào LiveData
                rating.setValue(String.valueOf(ratingData)); // Chuyển float thành String để set vào LiveData
                countRating.setValue(String.valueOf(countRatingData)); // Chuyển int thành String để set vào LiveData
                description.setValue(descriptionData);
            }
        });
    }

    public void updateRating(String booksId, int rating) {

        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Kiểm tra nếu đã tồn tại tài liệu với cùng booksId và userId trong HistoryBooks
            Query historyQuery = firebaseFirestore.collection("UserRating")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("booksId", booksId);

            historyQuery.get()
                    .addOnSuccessListener(querySnapshot -> {
                                if (querySnapshot.isEmpty()) {
                                    // Nếu không tồn tại, thêm tài liệu mới
                                    Map<String, Object> historyData = new HashMap<>();
                                    historyData.put("userId", userId);
                                    historyData.put("booksId", booksId);
                                    historyData.put("rating", rating);

                                    firebaseFirestore.collection("UserRating")
                                            .add(historyData)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    firebaseFirestore.collection("Books")
                                                            .document(booksId)
                                                            .update("ratingsCount", FieldValue.increment(1))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    updateBooksRating(booksId);
                                                                }
                                                            });
                                                }
                                            });
                                } else {
                                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                    String historyDocumentId = documentSnapshot.getId();
                                    firebaseFirestore.collection("UserRating")
                                            .document(historyDocumentId)
                                            .update("rating", rating)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    updateBooksRating(booksId);
                                                }
                                            });
                                }
                    });
        }
    }

    // Hàm tính toán và cập nhật trường "Rating" trong collection "Books"
    private void updateBooksRating(String booksId) {
        // Lấy dữ liệu tổng rating và số lượt đánh giá từ collection "UserRating"
        firebaseFirestore.collection("UserRating")
                .whereEqualTo("booksId", booksId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int totalRating = 0;
                    int ratingsCount = querySnapshot.size();

                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        int userRating = documentSnapshot.getLong("rating").intValue();
                        totalRating += userRating;
                    }

                    Log.e("TONG DIEM", Integer.toString(totalRating));

                    // Tính toán trung bình rating
                    double averageRating = (double) totalRating / ratingsCount;

                    // Cập nhật lại trường "Rating" trong collection "Books"
                    firebaseFirestore.collection("Books")
                            .document(booksId)
                            .update("rating", averageRating)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loadBookData(booksId);
                                }
                            });
                });
    }
}
