package com.example.readbook.ui.chapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.readbook.databinding.ActivityChaptersBinding;
import com.example.readbook.models.Chapter;
import com.example.readbook.ui.chapter.settings.SettingsFragment;
import com.example.readbook.ui.main.MainActivity;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Chapters extends AppCompatActivity {
    private ActivityChaptersBinding binding;
    private ChapterMV chapterViewModel;
    private String booksId;
    private int maxChapter;
    private Chapter currentChapter;
    private int currentChapterNumber = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChaptersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadChapter();
        setUpObservers();
        binding.btnNextChapter.setOnClickListener(view -> onNextChapterClicked());
        binding.btnPreviousChapter.setOnClickListener(view -> onPreviousChapterClicked());

        binding.btnSetting.setOnClickListener(view -> openSettingsFragment());
        binding.icHome.setOnClickListener(view -> backHome());
    }

    private void backHome() {
        Intent intent = new Intent(Chapters.this, MainActivity.class);
        startActivity(intent);
    }

    private void loadChapter() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            booksId = extras.getString("booksId");
        }
        chapterViewModel = new ViewModelProvider(this).get(ChapterMV.class);
    }
    private void setUpObservers() {
        chapterViewModel.getChapterLiveData().observe(this, documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                currentChapter = documentSnapshot.toObject(Chapter.class);
                if (currentChapter != null) {
                    binding.tvChapterTitle.setText(currentChapter.getChaptersName());
                    // Tính số chương tối đa
                    maxChapter = getMaxChapterNumber(currentChapter.getChaptersName());
                    Log.e("TỔNG SỐ CHƯƠNG", String.valueOf(maxChapter));
                    // Hiển thị nội dung chương hiện tại
                    loadChapterContent();
                }
            } else {
                Toast.makeText(this, "Không tìm thấy truyện", Toast.LENGTH_SHORT).show();
            }
        });
        chapterViewModel.getChapter(booksId, "Chương 1"); // Lấy chương đầu tiên khi mở activity
    }
    private int getMaxChapterNumber(String chapterName) {
        // Tách số chương từ tên chương (vd: Chương 1 -> 1)
        String[] parts = chapterName.split(" ");
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
    // Phương thức load nội dung chương từ file Word và hiển thị lên TextView
    private void loadChapterContent() {
        if (currentChapter != null) {
            new Thread(() -> {
                String content = readWordFile(currentChapter.getChaptersContent());

                // Hiển thị nội dung trong TextView
                runOnUiThread(() -> binding.tvChapterContent.setText(content));
            }).start();
        }
    }
    private String readWordFile(String fileUrl) {
        String content = "";
        try {
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();
            XWPFDocument document = new XWPFDocument(inputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            // Trích xuất nội dung từ tệp Word
            content = extractor.getText();
            // Đóng luồng đọc tệp
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    private void onPreviousChapterClicked() {
        if (currentChapterNumber > 1) {
            currentChapterNumber--;
            String chapterName = "Chương " + currentChapterNumber;
            chapterViewModel.getChapter(booksId, chapterName);
        }
    }
    private void onNextChapterClicked() {
        if (currentChapterNumber <= maxChapter) {
            currentChapterNumber++;
            String chapterName = "Chương " + currentChapterNumber;
            chapterViewModel.getChapter(booksId, chapterName);
        }
    }

    private void openSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();

        int currentTextSize = (int) binding.tvChapterContent.getTextSize();
        int currentTextColor = binding.tvChapterContent.getCurrentTextColor();
        int currentBackgroundColor = ((ColorDrawable) binding.tvChapterContent.getBackground()).getColor();
        float currentTextSizeSp = pxToSp((int) binding.tvChapterContent.getTextSize());

        Bundle bundle = new Bundle();
        bundle.putFloat("textSize", currentTextSizeSp);
        bundle.putInt("textColor", currentTextColor);
        bundle.putInt("backgroundColor", currentBackgroundColor);
        settingsFragment.setArguments(bundle);

        settingsFragment.show(getSupportFragmentManager(), settingsFragment.getTag());

        settingsFragment.setOnSettingsChangeListener((textSize, textColorResId, bgColorResId) -> {
            binding.tvChapterContent.setTextSize(textSize);
            binding.tvChapterContent.setTextColor(ContextCompat.getColor(this, textColorResId));
            binding.tvChapterContent.setBackgroundColor(ContextCompat.getColor(this, bgColorResId));
        });
    }
    private float pxToSp(float px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }
}