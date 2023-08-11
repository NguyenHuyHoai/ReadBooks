package com.example.readbook.ui.browse.changePass;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentChangePassBinding;
import com.example.readbook.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class ChangePassFragment extends Fragment {
    private FragmentChangePassBinding binding;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = firebaseFirestore.collection("Users");
    private Users itemUsers;
    ProgressDialog progressDialog;

    private String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePassBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getActivity());
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
        DocumentReference userRef = usersCollection.document(userId);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String email = document.getString("email");
                                binding.btnChangePass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checkPass(email);
                                    }
                                });
                            }
                        }
                    }
                });
        binding.cbShowpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updatePasswordVisibility(isChecked);
            }
        });
        updatePasswordVisibility(binding.cbShowpass.isChecked());
        setupButton();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();
            }
        });

        return binding.getRoot();
    }
    private void updatePasswordVisibility(boolean showPassword) {
        int inputType = showPassword ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

        binding.eTCpass.setInputType(inputType);
        binding.eTNpass.setInputType(inputType);
        binding.eTRNpass.setInputType(inputType);
    }
    //Setup button
    private void setupButton() {
        checkButtonState();
        // Thêm TextChangedListener cho các EditText
        binding.eTCpass.addTextChangedListener(textWatcher);
        binding.eTNpass.addTextChangedListener(textWatcher);
        binding.eTRNpass.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Khi text của bất kỳ EditText nào thay đổi, cập nhật trạng thái nút Change Password
            checkButtonState();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private void checkButtonState() {
        String oldpass = binding.eTCpass.getText().toString().trim();
        String newpass = binding.eTNpass.getText().toString().trim();
        String rnewpass = binding.eTRNpass.getText().toString().trim();

        boolean isAnyFieldEmpty = oldpass.isEmpty() || newpass.isEmpty() || rnewpass.isEmpty();

        if (isAnyFieldEmpty) {
            binding.btnChangePass.setEnabled(false);
            binding.btnChangePass.setBackgroundResource(R.drawable.button_disabled);
        } else {
            binding.btnChangePass.setEnabled(true);
            binding.btnChangePass.setBackgroundResource(R.drawable.button_enabled);
        }
    }
    //Xu li BtnChange
    private void checkPass(String email) {
        String oldpass = binding.eTCpass.getText().toString();
        String newpass = binding.eTNpass.getText().toString();
        String Rpass = binding.eTRNpass.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseAuth.signInWithEmailAndPassword(email, oldpass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";
                        if (!newpass.matches(passwordPattern)) {
                            binding.eTNpass.setError("Mật khẩu trên 6 kí tự và có cả chữ lẫn số!");
                            binding.eTNpass.requestFocus();
                            return;
                        }
                        if (!newpass.equals(Rpass)) {
                            binding.eTRNpass.setError("Mật khẩu không khớp");
                            binding.eTRNpass.requestFocus();
                            return;
                        }
                        if (newpass.equals(oldpass)) {
                            binding.eTNpass.setError("Phải khác mật khẩu mới!");
                            binding.eTNpass.requestFocus();
                            return;
                        }
                        progressDialog.show();
                        firebaseAuth.getCurrentUser()
                                .updatePassword(newpass)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Đã thay đổi mật khẩu thành công!",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                        binding.eTCpass.setText("");
                                        binding.eTNpass.setText("");
                                        binding.eTRNpass.setText("");
                                        setupButton();
                                    } else {
                                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thất bại!",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.eTCpass.setError("Vui lòng nhập đúng mật khẩu!");
                        binding.eTCpass.requestFocus();
                    }
                });
    }
}