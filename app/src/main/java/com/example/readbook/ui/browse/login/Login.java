package com.example.readbook.ui.browse.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.readbook.R;
import com.example.readbook.databinding.ActivityLoginBinding;
import com.example.readbook.ui.browse.forgotPass.ForgotPass;
import com.example.readbook.ui.browse.register.Register;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private LoginMV loginModelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        loginModelView = new LoginMV(this, progressDialog, binding);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginModelView.onLoginClicked(binding.eTEmail.getText().toString(),
                        binding.eTPass.getText().toString());
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        binding.tVForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPass.class));
            }
        });
    }
}
