package com.example.readbook.ui.browse.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.readbook.databinding.ActivityLoginBinding;
import com.example.readbook.models.Users;
import com.example.readbook.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import java.util.List;

public class LoginMV {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = firebaseFirestore.collection("Users");
    private Context context;
    private ProgressDialog progressDialog;

    private ActivityLoginBinding binding;

    public LoginMV(Context context, ProgressDialog progressDialog, ActivityLoginBinding binding) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.binding = binding;
    }

    public void onLoginClicked(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            binding.eTEmail.setError("Email không được để trống");
            binding.eTEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.eTPass.setError("Mật khẩu không được để trống");
            binding.eTPass.requestFocus();
            return;
        }

        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {
                                    checkAccount(email);
                                } else {
                                    binding.tvError.setText("");
                                    binding.tvError.setText("Vui lòng vào email xác minh rồi đăng nhập lại!");
                                    progressDialog.cancel();
                                }
                            }
                        } else {
                            Toast.makeText(context, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });
    }

    private void checkAccount(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = usersCollection.document(userId);
            userRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    boolean isAdmin = document.getBoolean("admin");
                                    boolean isLocked = document.getBoolean("locked");

                                    Log.e("KHOA", String.valueOf(isLocked));

                                    if (isLocked) {
                                        binding.tvError.setText("");
                                        binding.tvError.setText("Tài khoản đã bị khóa!");
                                        progressDialog.cancel();
                                    } else {
                                        String avatar = document.getString("avatar");
                                        String usersName = document.getString("usersName");
                                        String gender = document.getString("gender");
                                        String describe = document.getString("describe");
                                        String usersId = document.getString("usersId");
                                        List<String> followedBooks = (List<String>) document.get("followedBooks");
                                        List<String> recentlyViewedBooks = (List<String>) document.get("recentlyViewedBooks");

                                        Users itemUsers = new Users(usersId, avatar, usersName,
                                                email, gender, describe, isAdmin, isLocked, followedBooks, recentlyViewedBooks);

                                        Gson gson = new Gson();
                                        String itemUsersJson = gson.toJson(itemUsers);

                                        Intent intent = new Intent(context, MainActivity.class);
                                        context.startActivity(intent);
                                        progressDialog.cancel();
                                        ((Activity) context).finish();
                                    }
                                }
                            }
                        }
                    });
        }
    }

}

