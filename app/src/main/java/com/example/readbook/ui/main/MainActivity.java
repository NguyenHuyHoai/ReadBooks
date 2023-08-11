package com.example.readbook.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.readbook.R;
import com.example.readbook.databinding.ActivityMainBinding;
import com.example.readbook.models.Users;
import com.example.readbook.ui.browse.login.Login;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String userId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = firebaseFirestore.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(binding.navbar, navController);

        // Kiểm tra xem người dùng đã đăng nhập hay chưa khi chuyển đến bookmarks_fragment và browse_fragment
        binding.navbar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (itemId == R.id.bookmarks_fragment || itemId == R.id.browse_fragment || itemId == R.id.library_fragment) {
                if (currentUser == null) {
                    // Người dùng chưa đăng nhập, chuyển hướng sang màn hình đăng nhập
                    startActivity(new Intent(this, Login.class));
                } else {
                    Bundle bundle = new Bundle();
                    userId = currentUser.getUid();
                    bundle.putString("userId", userId);
                    navController.navigate(itemId, bundle);
                }
            } else {
                if (currentUser == null) {
                    navController.navigate(itemId);
                } else {
                    Bundle bundle = new Bundle();
                    userId = currentUser.getUid();
                    bundle.putString("userId", userId);
                    navController.navigate(itemId, bundle);
                }
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MenuItem menuItem = binding.navbar.getMenu().findItem(R.id.explore_fragment);
        menuItem.setChecked(true);
    }
}

