package com.example.readbook.ui.detail.tablayout.chapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readbook.R;
import com.example.readbook.databinding.FragmentBookmarksBinding;
import com.example.readbook.databinding.FragmentChapterListBinding;
import com.example.readbook.models.Chapter;
import com.example.readbook.ui.chapter.Chapters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class ChapterListFragment extends Fragment {

    private ChapterListViewModel viewModel;
    private FragmentChapterListBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String booksId;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChapterListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy booksId từ Bundle
        if (getArguments() != null) {
            booksId = getArguments().getString("booksId");
        }

        ChapterListAdapter adapter = new ChapterListAdapter();
        binding.bookChapterList.setAdapter(adapter);

        // Ví dụ thiết lập LayoutManager cho RecyclerView (chỉ là ví dụ, bạn cần thay thế bằng thực tế của bạn)
        binding.bookChapterList.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Bắt sự kiện click trên chương
        adapter.setOnChapterClickListener(chapter -> {
            // Mở fragment Chapters để xem nội dung của chương
            openChapterFragment(chapter);
        });

        // Khởi tạo ViewModel và gọi phương thức loadChapters
        viewModel = new ViewModelProvider(this).get(ChapterListViewModel.class);
        viewModel.loadChapters(booksId);

        // Theo dõi LiveData để cập nhật danh sách chương
        viewModel.getChapterListLiveData().observe(getViewLifecycleOwner(), chapters -> {
            adapter.setChapterList(chapters);
        });
    }

    private void openChapterFragment(Chapter chapter) {
        firebaseFirestore.collection("Books")
                .document(booksId)
                .update("viewsCount", FieldValue.increment(1));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Books").document(booksId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String booksName = document.getString("booksName");
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            userId = currentUser.getUid();
                            firebaseFirestore.collection("Users")
                                    .document(userId)
                                    .update("recentlyViewedBooks", FieldValue.arrayUnion(booksName));
                        }
                    }
                }
            }
        });



        // Kiểm tra nếu đã tồn tại tài liệu với cùng booksId và userId trong HistoryBooks
        Query historyQuery = firebaseFirestore.collection("HistoryBooks")
                .whereEqualTo("userId", userId)
                .whereEqualTo("booksId", booksId);

        historyQuery.get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        // Nếu không tồn tại, thêm tài liệu mới
                        Map<String, Object> historyData = new HashMap<>();
                        historyData.put("userId", userId);
                        historyData.put("booksId", booksId);
                        historyData.put("timeView", FieldValue.serverTimestamp());

                        firebaseFirestore.collection("HistoryBooks")
                                .add(historyData);
                    } else {
                        // Nếu đã tồn tại, cập nhật trường timeView
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String historyDocumentId = documentSnapshot.getId();
                        firebaseFirestore.collection("HistoryBooks")
                                .document(historyDocumentId)
                                .update("timeView", FieldValue.serverTimestamp());
                    }

                    // Mở Fragment Chapters hoặc thực hiện hành động khác
                    Bundle bundle = new Bundle();
                    bundle.putString("booksId", booksId);
                    bundle.putString("chaptersName", chapter.getChaptersName());
                    requireActivity().startActivity(new Intent(requireActivity(), Chapters.class).putExtras(bundle));
                })
                .addOnFailureListener(e -> {
                    Log.e("DetailFragment", "Error checking history", e);
                });
    }
}

