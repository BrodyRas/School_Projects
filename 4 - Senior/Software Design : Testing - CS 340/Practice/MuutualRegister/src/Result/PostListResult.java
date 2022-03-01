package Result;

import Model.Post;
import Model.PostSkel;

import java.text.ParseException;
import java.util.ArrayList;

public class PostListResult {
    public PostListResult(){}

    public PostListResult(ArrayList<PostSkel> posts) throws ParseException {
        this.posts = posts;
    }

    public PostListResult(String message) {
        this.message = message;
    }

    public String message;
    public ArrayList<PostSkel> posts;
}
