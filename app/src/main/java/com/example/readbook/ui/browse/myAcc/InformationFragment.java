package com.example.readbook.ui.browse.myAcc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.readbook.R;
import com.example.readbook.databinding.FragmentInformationBinding;
import com.example.readbook.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;


public class InformationFragment extends Fragment {
    private FragmentInformationBinding binding;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = firebaseFirestore.collection("Users");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference().child("avatars");

    private TextView tvUsenameMP;
    private TextView tvMota;
    private Spinner spinnerGender;
    private Bitmap previousImageBitmap;
    private String previousUsername;
    private String previousGender;
    private String previousMota;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int REQUEST_IMAGE_PICK = 4;

    private Users itemUsers;
    private String userId;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getActivity());
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            DocumentReference userRef = usersCollection.document(userId);
            userRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String email = document.getString("email");
                                    String avatar = document.getString("avatar");
                                    String usersName = document.getString("usersName");
                                    String gender = document.getString("gender");
                                    String describe = document.getString("describe");
                                    displayUser(email, avatar, usersName, gender, describe);
                                    setupSaveButton();
                                    binding.btnFileImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            openImageChooser();
                                        }
                                    });
                                    binding.btnSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            progressDialog.show();
                                            uploadImageToStorage(avatar);
                                        }
                                    });
                                }
                            }
                        }
                    });
        }
        return binding.getRoot();
    }
    private void displayUser(String email, String avatar, String usersName, String gender, String describe) {
        binding.tvUsenameMP.setText(usersName);
        binding.tvEmailMP.setText(email);
        binding.tvMota.setText(describe);

        String imageRef = avatar;
        Glide.with(this)
                .load(imageRef)
                .placeholder(R.drawable.icon_load)
                .error(R.drawable.defaultavatar)
                .into(binding.imageMP);
        if (gender != null) {
            if (gender.equals("Nam")) {
                binding.spinnerGender.setSelection(0);
            } else if (gender.equals("Nữ")) {
                binding.spinnerGender.setSelection(1);
            } else if (gender.equals("Khác")) {
                binding.spinnerGender.setSelection(2);
            }
        }
    }

    //Điều chỉnh button
    private void setupSaveButton() {
        // Khởi tạo các biến và lưu giá trị ban đầu của các trường
        tvUsenameMP = binding.tvUsenameMP;
        spinnerGender = binding.spinnerGender;
        tvMota = binding.tvMota;

        previousImageBitmap = null;
        previousUsername = tvUsenameMP.getText().toString();
        previousGender = (String) spinnerGender.getSelectedItem();
        previousMota = tvMota.getText().toString();

        // Ban đầu, ẩn btnSave
        binding.btnSave.setEnabled(false);
        binding.btnSave.setBackgroundResource(R.drawable.button_disabled);

        // Listener cho trường tvUsenameMP
        tvUsenameMP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // Listener cho spinnerGender
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Listener cho trường tvMota
        tvMota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private boolean checkImageChanged() {
        if (previousImageBitmap != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUsernameChanged() {
        String currentUsername = tvUsenameMP.getText().toString();
        return !currentUsername.equals(previousUsername);
    }

    private boolean checkGenderChanged() {
        String currentGender = (String) spinnerGender.getSelectedItem();
        return !currentGender.equals(previousGender);
    }

    private boolean checkMotaChanged() {
        String currentMota = tvMota.getText().toString();
        return !currentMota.equals(previousMota);
    }

    private void checkButtonState() {
        boolean isChanged = checkUsernameChanged() || checkGenderChanged() || checkMotaChanged() || checkImageChanged();
        binding.btnSave.setEnabled(isChanged);
        if (isChanged) {
            binding.btnSave.setBackgroundResource(R.drawable.button_enabled);
        } else {
            binding.btnSave.setBackgroundResource(R.drawable.button_disabled);
        }
    }

    //Input Image
    private void openImageChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn ảnh");
        String[] options = {"Chụp ảnh", "Chọn ảnh từ thiết bị"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        // Chọn chụp ảnh
                        if (checkCameraPermission()) {
                            dispatchTakePictureIntent();
                        }
                        break;
                    case 1:
                        // Chọn ảnh từ thiết bị
                        if (checkExternalStoragePermission()) {
                            openGallery();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu quyền từ người dùng
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu quyền từ người dùng
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                binding.imageMP.setImageBitmap(imageBitmap);

                previousImageBitmap = imageBitmap;
                checkButtonState();
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri imageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                    binding.imageMP.setImageBitmap(imageBitmap);

                    previousImageBitmap = imageBitmap;
                    checkButtonState();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Out Image
    private void uploadImageToStorage(String avatar) {
        if (previousImageBitmap != null) {
            // Tạo ByteArrayOutputStream để nén ảnh thành một mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            previousImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            String filename = UUID.randomUUID().toString() + ".jpg";

            StorageReference imageRef = storageRef.child(filename);
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Ở đây, bạn có thể lấy URL và cập nhật thông tin vào Collection của tài khoản đang đăng nhập
                            String imageUrl = uri.toString();
                            updateUserInfo(imageUrl);
                        }
                    });
                }
            });
        } else {
            updateUserInfo(avatar);
        }
    }

    private void updateUserInfo(String imageUrl) {
        String newGender = (String) spinnerGender.getSelectedItem();
        String newMota = tvMota.getText().toString();
        usersCollection.document(userId)
                .update(
                        "gender", newGender,
                        "describe", newMota,
                        "avatar", imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.cancel();
                        showSuccessDialog();
                        setupSaveButton();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        showFailureDialog();
                    }
                });
    }

    // Hàm hiển thị Dialog thành công và thất bại
    private void showFailureDialog() {
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icon_not_verified)
                .setTitle("Bạn đã cập nhật thông tin thất bại!")
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icon_verified)
                .setTitle("Bạn đã cập nhật thông tin thành công!")
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}