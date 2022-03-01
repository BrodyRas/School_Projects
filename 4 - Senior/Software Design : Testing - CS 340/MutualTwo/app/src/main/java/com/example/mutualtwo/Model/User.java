package com.example.mutualtwo.Model;

import java.util.HashSet;
import java.util.Set;

public class User {
    public User(){}
    public User(String firstName, String lastName, String username, String password, String pictureURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.pictureURL = pictureURL;

        followers = new HashSet<>();
        following = new HashSet<>();
    }
    private String firstName, lastName;
    private String username, password, pictureURL;
    private Set<String> followers, following;

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String fullName() {
        return firstName + " " + lastName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPictureURL() {
        return pictureURL;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Set<String> getFollowers() {
        return followers;
    }
    public void addFollower(String username) {
        followers.add(username);
    }

    public Set<String> getFollowing() {
        return following;
    }
    public void addFollowing(String username) {
        following.add(username);
    }

    public void removeFollower(String username){
        followers.remove(username);
    }
    public void removeFollowing(String username){
        following.remove(username);

    }

}
