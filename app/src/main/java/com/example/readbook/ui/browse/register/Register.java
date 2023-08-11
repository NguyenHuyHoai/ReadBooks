package com.example.readbook.ui.browse.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.readbook.R;
import com.example.readbook.databinding.ActivityRegisterBinding;
import com.example.readbook.ui.browse.login.Login;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;
    private RegisterMV registerModelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        registerModelView = new RegisterMV(this, progressDialog, binding);

        binding.btnRegisterMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerModelView.onRegisterClicked(binding.eTUser.getText().toString(),
                        binding.eTEmail.getText().toString(),
                        binding.eTPass.getText().toString(),
                        binding.eTRPass.getText().toString());
            }
        });

        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}
