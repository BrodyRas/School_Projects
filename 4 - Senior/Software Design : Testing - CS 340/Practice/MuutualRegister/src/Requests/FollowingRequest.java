package Requests;

public class FollowingRequest {
    FollowingRequest(){}

    public FollowingRequest(String username, String type) {
        this.username = username;
        this.type = type;
    }

    public String username;
    public String type;
}
