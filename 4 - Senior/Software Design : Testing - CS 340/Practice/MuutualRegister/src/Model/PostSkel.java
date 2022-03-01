package Model;

import java.text.ParseException;

public class PostSkel {
    public PostSkel(String username,
                    String firstName,
                    String lastName,
                    String profilePictureURL,
                    String body,
                    String attachment,
                    String timeString) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureURL = profilePictureURL;
        this.body = body;
        this.attachment = attachment;
        this.timeString = timeString;
    }

    public String username, firstName, lastName, profilePictureURL, body, attachment, timeString;

    public Post returnPost() throws ParseException {
        return new Post(username,firstName,lastName,profilePictureURL,body,attachment,timeString);
    }
}
