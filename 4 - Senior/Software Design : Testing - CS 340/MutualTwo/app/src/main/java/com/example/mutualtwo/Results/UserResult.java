package com.example.mutualtwo.Results;


import com.example.mutualtwo.Model.User;

public class UserResult {
    public UserResult(){}
    public UserResult(User user) {
        this.user = user;
    }

    public UserResult(String message) {
        this.message = message;
    }

    public String message;
    public User user;
}
