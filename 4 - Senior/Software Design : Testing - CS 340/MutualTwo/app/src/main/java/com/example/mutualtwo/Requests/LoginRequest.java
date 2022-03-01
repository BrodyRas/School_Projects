package com.example.mutualtwo.Requests;

public class LoginRequest {
    public LoginRequest(){}
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username;
    public String password;
}
