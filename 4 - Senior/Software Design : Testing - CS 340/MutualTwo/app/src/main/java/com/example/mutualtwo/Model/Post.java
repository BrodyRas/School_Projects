package com.example.mutualtwo.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Post implements Comparable<Post>{
    private String username;
    private String firstName;
    private String lastName;
    private String body;
    private String profilePictureURL;
    private Set<String> hashtags;
    private String attachment;
    private Date date;
    private String timeString;

    public Post(){}
    public Post(User user, String body, String attachment) {
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();

        this.body = body;
        this.profilePictureURL = user.getPictureURL();
        this.attachment = attachment;

        hashtags = new HashSet<>();

        String pattern = "MM/dd '|' hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        date = new Date();
        timeString = format.format(date);

        parseBody();
    }

    public Post(String username,
                String firstName,
                String lastName,
                String profilePictureURL,
                String body,
                String attachment,
                String timeString)
            throws ParseException {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureURL = profilePictureURL;

        this.body = body;
        this.attachment = attachment;
        this.timeString = timeString;

        hashtags = new HashSet<>();

        String pattern = "MM/dd '|' hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        date = format.parse(timeString);

        parseBody();
    }

    public void parseTime(){
        try {
            String pattern = "MM/dd '|' hh:mm a";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseBody(){
        for(int i = 0; i < body.length(); i++){
            //HASHTAGS
            if(body.charAt(i) == '#'){
                i++; //skip the #
                StringBuilder sb = new StringBuilder();
                while(i < body.length() && body.charAt(i) != ' ' && Character.isLetter(body.charAt(i))){
                    sb.append(body.charAt(i));
                    i++;
                }
                //Only add substantive mentions
                String mention = sb.toString();
                if(!mention.equals("")){
                    hashtags.add(mention.toLowerCase());
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }
    public String getFirstName() {
        return firstName;
    }
    public String fullName() {
        return firstName + " " + lastName;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getProfilePictureURL() {
        return profilePictureURL;
    }
    public String getAttachment() {
        return attachment;
    }
    public String getTimeString(){return timeString;}
    public Date getDate(){return date;}

    public Set<String> getHashtags() {
        return hashtags;
    }

    @Override
    public int compareTo(Post o) {
        return timeString.compareTo(o.getTimeString());
    }
}
