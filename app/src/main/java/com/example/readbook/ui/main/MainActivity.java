package com.example.readbook.ui.main;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.readbook.R;
import com.example.readbook.connected.NetworkChangeReceiver;
import com.example.readbook.databinding.ActivityMainBinding;
import com.example.readbook.ui.bookmarks.BookmarksFragment;
import com.example.readbook.ui.browse.BrowseFragment;
import com.example.readbook.ui.explore.ExploreFragment;
import com.example.readbook.ui.library.LibraryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private BroadcastReceiver networkChangeReceiver;
    private View errorView; // Layout error

    // Khai báo biến để lưu trạng thái trang hiện tại
    private Fragment currentFragment;
    private boolean isInternetConnected;

    private boolean isConnectedBefore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }
}