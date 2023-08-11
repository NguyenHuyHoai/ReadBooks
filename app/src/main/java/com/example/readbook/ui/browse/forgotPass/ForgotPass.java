package com.example.readbook.ui.browse.forgotPass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.readbook.R;
import com.example.readbook.databinding.ActivityForgotPassBinding;
import com.example.readbook.ui.browse.login.Login;

public class ForgotPass extends AppCompatActivity {

    private ActivityForgotPassBinding binding;
    private ProgressDialog progressDialog;
    private ForgotMV forgotModelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        forgotModelView = new ForgotMV(this, progressDialog, binding);

        binding.btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotModelView.onSentClicked(binding.eTEmail.getText().toString());
            }
        });

        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPass.this, Login.class));
            }
        });
    }
}
