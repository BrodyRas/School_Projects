package com.example.mutualtwo.Utility;

import com.example.mutualtwo.Requests.FollowRequest;
import com.example.mutualtwo.Requests.FollowingRequest;
import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Requests.LoginRequest;
import com.example.mutualtwo.Requests.PictureRequest;
import com.example.mutualtwo.Requests.PostRequest;
import com.example.mutualtwo.Requests.UserRequest;
import com.example.mutualtwo.Results.AuthResult;
import com.example.mutualtwo.Results.FollowResult;
import com.example.mutualtwo.Results.GeneralResult;
import com.example.mutualtwo.Results.PostListResult;
import com.example.mutualtwo.Results.UserResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerProxy {

    public AuthResult register(UserRequest request) {
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully registered!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, AuthResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthResult logIn(LoginRequest request) {
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully logged in!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, AuthResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserResult getUser(GeneralRequest request){
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/getuser");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully found!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, UserResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PostListResult getFeed(GeneralRequest request) {
        return getPosts("FEED", request);
    }

    public GeneralResult makePost(PostRequest request) {
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/makepost");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully found!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, GeneralResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PostListResult getHashtaggedPosts(GeneralRequest request) {
        return getPosts("HASH", request);
    }

    public PostListResult getStory(GeneralRequest request) {
        return getPosts("STORY", request);
    }

    public GeneralResult changePicture(PictureRequest request) {
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/changepicture");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully registered in!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, GeneralResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GeneralResult setFollow(FollowRequest request) {
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/setfollow");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully registered in!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, GeneralResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FollowResult getFollows(FollowingRequest request){
        try {
            URL url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/getfollows");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Users Found!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            FollowResult result = gson.fromJson(resultText, FollowResult.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PostListResult getPosts(String type, GeneralRequest request){
        try {
            URL url = null;
            switch (type){
                case "FEED":
                    url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/getfeed");
                    break;
                case "STORY":
                    url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/getstory");
                    break;
                case "HASH":
                    url = new URL("https://clkcv9euve.execute-api.us-east-2.amazonaws.com/Prod/gethashedposts");
                    break;
            }
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Posts Found!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            PostListResult result = gson.fromJson(resultText, PostListResult.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Convenience functions used to access Request/Response bodies
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
