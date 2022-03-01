package Requests;

import Model.Post;

import java.util.List;

public class PageRequest {
    public PageRequest(){}
    public PageRequest(List<String> values, Post post) {
        this.values = values;
        this.post = post;
    }

    public List<String> values;
    public Post post;
}
