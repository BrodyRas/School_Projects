package com.example.mutualtwo.Requests;


import com.example.mutualtwo.Model.User;

public class UserRequest {
    public UserRequest(){}
    public UserRequest(User user) {
        this.user = user;
    }
    public User user;
}
