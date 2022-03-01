package com.example.mutualtwo.Requests;


public class PostRequest {
    public PostRequest(){}

    public PostRequest(String username, String body, String attachment) {
        this.username = username;
        this.body = body;
        this.attachment = attachment;
    }

    public String username;
    public String body;
    public String attachment;
}
