package com.example.readbook.models;

import com.google.firebase.Timestamp;

public class Chapter {
    private String chaptersId;
    private String booksId;
    private String chaptersName;
    private String chaptersContent;
    private Timestamp chaptersTime;
    private int Stt;

    private int extractChapterNumber() {
        String[] parts = chaptersName.split(" ");
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }
    public Chapter() {
    }
    public Chapter(String chaptersId, String booksId, String chaptersName,
                   String chaptersContent, Timestamp chaptersTime) {
        this.chaptersId = chaptersId;
        this.booksId = booksId;
        this.chaptersName = chaptersName;
        this.chaptersContent = chaptersContent;
        this.chaptersTime = chaptersTime;
        this.Stt = extractChapterNumber();
    }

    public String getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(String chaptersId) {
        this.chaptersId = chaptersId;
    }

    public String getBooksId() {
        return booksId;
    }

    public void setBooksId(String booksId) {
        this.booksId = booksId;
    }

    public String getChaptersName() {
        return chaptersName;
    }

    public void setChaptersName(String chaptersName) {
        this.chaptersName = chaptersName;
    }

    public String getChaptersContent() {
        return chaptersContent;
    }

    public void setChaptersContent(String chaptersContent) {
        this.chaptersContent = chaptersContent;
    }

    public Timestamp getChaptersTime() {
        return chaptersTime;
    }

    public void setChaptersTime(Timestamp chaptersTime) {
        this.chaptersTime = chaptersTime;
    }

    public int getStt() {
        return Stt;
    }

    public void setStt(int stt) {
        Stt = stt;
    }
}
