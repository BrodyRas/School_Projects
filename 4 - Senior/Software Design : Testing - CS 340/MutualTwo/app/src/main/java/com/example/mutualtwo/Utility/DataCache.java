package com.example.mutualtwo.Utility;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.mutualtwo.Model.Name;
import com.example.mutualtwo.Model.Post;
import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Results.UserResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataCache {
    private static DataCache instance;

    private Map<String, Bitmap> cachedPictures;
    private Post clickedPost;
    private User loggedUser;

    private DataCache() {
        cachedPictures = new HashMap<>();
    }

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    public void setPicture(String url, ImageView iv) {
        Bitmap myBitmap = cachedPictures.get(url);
        if (myBitmap == null) {
            PhotoRetrieverTask prt = new PhotoRetrieverTask();
            prt.setBM(myBitmap);
            prt.setIV(iv);
            prt.execute(url);
        } else {
            iv.setImageBitmap(myBitmap);
        }
    }
    public void putPic(String url, Bitmap bm) {
        cachedPictures.put(url, bm);
    }

    public Post getClickedPost() {
        return clickedPost;
    }
    public void setClickedPost(Post clickedPost) {
        this.clickedPost = clickedPost;
    }

    public User getLoggedUser() {
        return loggedUser;
    }
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void clearCache() {
        instance = null;
    }
}
