package com.example.mutualtwo.Results;

        import com.example.mutualtwo.Model.User;

        import java.util.ArrayList;

public class FollowResult {
    FollowResult(){}
    public FollowResult(String message) {
        this.message = message;
    }
    public FollowResult(ArrayList<User> users) {
        this.users = users;
    }

    public String message;
    public ArrayList<User> users;
}
