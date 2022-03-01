package Results;

import Models.Person;
import Models.User;

import java.util.Arrays;
import java.util.Objects;

public class UserResult {
    public UserResult(User user) {
        this.user = user;
    }

    public UserResult(String message) {
        this.message = message;
    }

    private User user;
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
