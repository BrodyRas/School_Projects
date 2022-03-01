package Requests;

public class PictureRequest {
    public PictureRequest(){}
    public PictureRequest(String username, String URL) {
        this.username = username;
        this.URL = URL;
    }

    public String username;
    public String URL;
}
