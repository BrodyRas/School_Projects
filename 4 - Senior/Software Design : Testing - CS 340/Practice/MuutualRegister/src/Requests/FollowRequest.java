package Requests;

public class FollowRequest {
    public FollowRequest(){}

    public FollowRequest(String username, String target, boolean state) {
        this.username = username;
        this.target = target;
        this.state = state;
    }

    public String username;
    public String target;
    public boolean state;
}
