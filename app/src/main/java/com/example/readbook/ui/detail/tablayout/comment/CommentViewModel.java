package com.example.readbook.ui.detail.tablayout.comment;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Comment;
import com.example.readbook.ui.browse.login.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CommentViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Comment>> commentListLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    public void navigateToLogin() {
        navigateToLogin.postValue(true);
    }

    public LiveData<List<Comment>> getCommentListLiveData() {
        return commentListLiveData;
    }

    public void loadComments(String booksId) {
        db.collection("Comments")
                .whereEqualTo("booksId", booksId)
                .orderBy("commentTime", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Comment> comments = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Comment comment = documentSnapshot.toObject(Comment.class);
                        comments.add(comment);
                    }
                    commentListLiveData.setValue(comments);
                })
                .addOnFailureListener(e -> {

                });
    }

    public void postComment(String booksId, String commentText) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("Users").document(userId);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String usersName = documentSnapshot.getString("usersName");
                        String avatar = documentSnapshot.getString("avatar");
                        Comment comment = new Comment(booksId, userId, commentText, Timestamp.now(), usersName, avatar);

                        db.collection("Comments")
                                .add(comment)
                                .addOnSuccessListener(documentReference -> {
                                    loadComments(booksId);
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý lỗi nếu cần
                                });
                    }
                }
            });
        }
        else
        {
            navigateToLogin();
        }
    }
}

