package com.example.readbook.models;

import java.util.List;

public class Users {
    public String usersId;
    public String avatar;
    public String usersName;
    public String email;
    public String gender;
    public String describe;
    public boolean admin;
    public boolean locked;
    public List<String> followedBooks;
    public List<String> recentlyViewedBooks;

    public Users() {
    }

    public Users(String usersId, String avatar, String usersName, String email, String gender,
                 String describe, boolean admin, boolean locked,
                 List<String> followedBooks, List<String> recentlyViewedBooks) {
        this.usersId = usersId;
        this.avatar = avatar;
        this.usersName = usersName;
        this.email = email;
        this.gender = gender;
        this.describe = describe;
        this.admin = admin;
        this.locked = locked;
        this.followedBooks = followedBooks;
        this.recentlyViewedBooks = recentlyViewedBooks;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<String> getFollowedBooks() {
        return followedBooks;
    }

    public void setFollowedBooks(List<String> followedBooks) {
        this.followedBooks = followedBooks;
    }

    public List<String> getRecentlyViewedBooks() {
        return recentlyViewedBooks;
    }

    public void setRecentlyViewedBooks(List<String> recentlyViewedBooks) {
        this.recentlyViewedBooks = recentlyViewedBooks;
    }
}
