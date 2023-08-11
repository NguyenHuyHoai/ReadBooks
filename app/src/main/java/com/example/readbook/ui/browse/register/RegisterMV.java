package com.example.readbook.ui.browse.register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.readbook.databinding.ActivityRegisterBinding;
import com.example.readbook.models.Users;
import com.example.readbook.ui.browse.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class RegisterMV {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = firebaseFirestore.collection("Users");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference().child("avatars");
    private Context context;
    private ProgressDialog progressDialog;
    private ActivityRegisterBinding binding;

    public RegisterMV(Context context, ProgressDialog progressDialog, ActivityRegisterBinding binding) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.binding = binding;
    }

    public void onRegisterClicked(String usersname, String email, String password, String Rpass) {
        if (TextUtils.isEmpty(usersname)) {
            binding.eTUser.setError("Tên tài khoản không được để trống");
            binding.eTUser.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.eTEmail.setError("Email không được để trống");
            binding.eTEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith("@gmail.com")) {
            binding.eTEmail.setError("Email không hợp lệ");
            binding.eTEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.eTPass.setError("Mật khẩu không được để trống");
            binding.eTPass.requestFocus();
            return;
        }
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";
        if (!password.matches(passwordPattern)) {
            binding.eTPass.setError("Mật khẩu trên 6 kí tự và có cả chữ lẫn số!");
            binding.eTPass.requestFocus();
            return;
        }
        if (!password.equals(Rpass)) {
            binding.eTRPass.setError("Mật khẩu không khớp");
            binding.eTRPass.requestFocus();
            return;
        }

        progressDialog.show();

        usersCollection.whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            binding.tvErrorDK.setText("");
                            binding.tvErrorDK.setText("Email đã được sử dụng!");
                            progressDialog.cancel();
                        } else {
                            usersCollection.whereEqualTo("usersName", usersname).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                                binding.tvErrorDK.setText("");
                                                binding.tvErrorDK.setText("Tên tài khoản đã được sử dụng!");
                                                progressDialog.cancel();
                                            } else {
                                                createUser(email, password, usersname);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void createUser(String email, String password, String usersname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        String userId = authResult.getUser().getUid();

                        String imageResourceName = "icon_dep";
                        String imageUri = "android.resource://" + context.getPackageName() + "/drawable/" + imageResourceName;
                        StorageReference imageRef = storageRef.child("image.jpg");
                        UploadTask uploadTask = imageRef.putFile(Uri.parse(imageUri));
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        usersCollection.document(userId)
                                                .set(new Users(userId, imageUrl, usersname, email, "Nam", "", false,
                                                        false, new ArrayList<>(), new ArrayList<>()));
                                        context.startActivity(new Intent(context, Login.class));
                                        progressDialog.cancel();
                                        ((Activity) context).finish();
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Đăng kí thất bại!", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }
}

