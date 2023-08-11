package com.example.readbook.ui.browse.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Users;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Users> loggedInUser = new MutableLiveData<>();

    public LiveData<Users> getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Users user) {
        loggedInUser.setValue(user);
    }
}