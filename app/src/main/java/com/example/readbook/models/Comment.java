package com.example.readbook.models;

import com.google.firebase.Timestamp;
import com.google.type.Date;

public class Comment {
    private String booksId;
    private String usersId;
    private String commentContent;
    private Timestamp commentTime;
    private String usersName;
    private String avatar;

    public Comment() {
    }

    public Comment(String booksId, String usersId, String commentContent,
                   Timestamp commentTime, String usersName, String avatar) {
        this.booksId = booksId;
        this.usersId = usersId;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.usersName = usersName;
        this.avatar = avatar;
    }

    public String getBooksId() {
        return booksId;
    }

    public void setBooksId(String booksId) {
        this.booksId = booksId;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
