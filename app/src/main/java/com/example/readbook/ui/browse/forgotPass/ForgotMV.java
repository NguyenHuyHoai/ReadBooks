package com.example.readbook.ui.browse.forgotPass;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.readbook.databinding.ActivityForgotPassBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotMV {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Context context;
    private ProgressDialog progressDialog;
    private ActivityForgotPassBinding binding;

    public ForgotMV(Context context, ProgressDialog progressDialog, ActivityForgotPassBinding binding) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.binding = binding;
    }

    public void onSentClicked(String email) {
        if (TextUtils.isEmpty(email)) {
            binding.eTEmail.setError("Email không được để trống");
            binding.eTEmail.requestFocus();
            return;
        }

        progressDialog.setTitle("Đang gửi Email !!! ");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.cancel();
                        Toast.makeText(context, "Email đã được gửi thành công !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(context, "Email chưa đăng ký hoặc không tồn tại !", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

