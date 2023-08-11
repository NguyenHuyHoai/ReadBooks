package com.example.readbook.ui.browse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.readbook.R;
import com.example.readbook.databinding.FragmentBrowseBinding;
import com.example.readbook.databinding.FragmentDetailBinding;
import com.example.readbook.databinding.FragmentExploreBinding;
import com.example.readbook.models.Users;
import com.example.readbook.ui.explore.ExploreMV;
import com.example.readbook.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;


public class BrowseFragment extends Fragment {
    private FragmentBrowseBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = firebaseFirestore.collection("Users");
    private Users itemUsers;
    private String userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBrowseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
        Log.e("MONG THANH CONG", userId);
        OnClickButton();
    }

    private void OnClickButton() {
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });

        binding.changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePass();
            }
        });
        binding.myInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyInformation();
            }
        });
    }
    private void showMyInformation() {
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        controller.navigate(R.id.action_browse_fragment_to_myInformation_fragment, bundle);
    }

    private void showChangePass() {
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        controller.navigate(R.id.action_browse_fragment_to_changePass_fragment, bundle);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.icon_not_verified)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                })
                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}