package com.example.mutualtwo.Results;

import com.example.mutualtwo.Model.Post;

import java.text.ParseException;
import java.util.ArrayList;

public class PostListResult {
    public PostListResult(){}

    public PostListResult(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public PostListResult(String message) {
        this.message = message;
    }

    public String message;
    public ArrayList<Post> posts;
}
