package Requests;

import Models.User;

public class RegisterRequest {
    public RegisterRequest(User user) {
        this.user = user;
    }

    User user;

    public User getUser() {
        return user;
    }
}
