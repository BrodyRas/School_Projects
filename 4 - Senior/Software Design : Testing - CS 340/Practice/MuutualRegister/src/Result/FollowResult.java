package Result;

import Model.User;

import java.util.ArrayList;
import java.util.List;

public class FollowResult {
    FollowResult(){}
    public FollowResult(String message) {
        this.message = message;
    }
    public FollowResult(List<User> users) {
        this.users = users;
    }

    public String message;
    public List<User> users;
}
