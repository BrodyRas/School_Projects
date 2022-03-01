package com.example.mutualtwo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Requests.FollowRequest;
import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Requests.PictureRequest;
import com.example.mutualtwo.Results.GeneralResult;
import com.example.mutualtwo.Results.UserResult;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Utility.ServerProxy;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.mutualtwo.Activity.RegisterFragment.GET_FROM_GALLERY;

public class UserActivity extends AppCompatActivity {
    User user;
    String username;
    ImageView userPicture;
    TextView userTitle;
    Button changePictureButton, followButton, followers, following;

    public UserActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Get user data
        username = getIntent().getStringExtra("USERNAME");
        UserTask userTask = new UserTask();
        userTask.execute(username);
        //user = DataCache.getInstance().getUser(username);

        //Load in the User's story
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.user_frame);
        if (fragment == null) {
            fragment = new FeedFragment();
            Bundle args = new Bundle();
            args.putString("USERNAME", username);
            args.putString("TYPE", "STORY");
            fragment.setArguments(args);
            fm.beginTransaction().add(R.id.user_frame, fragment).commit();
        }

        //Get references
        userPicture = findViewById(R.id.user_picture);
        userTitle = findViewById(R.id.user_title);
        changePictureButton = findViewById(R.id.user_change_picture_button);
        followButton = findViewById(R.id.user_follow_button);
        followers = findViewById(R.id.user_follower_button);
        following = findViewById(R.id.user_following_button);
    }

    public void setValues() {
        String url = user.getPictureURL();
        final User loggedUser = DataCache.getInstance().getLoggedUser();
        DataCache.getInstance().setPicture(url, userPicture);
        if (!username.equals(loggedUser.getUsername())) {
            changePictureButton.setVisibility(View.GONE);
            setFollowButtonText();
        } else {
            followButton.setVisibility(View.GONE);
        }

        userTitle.setText(user.fullName());
        final String followersTitle = "Followers: " + user.getFollowers().size();
        followers.setText(followersTitle);
        String followingTitle = "Following: " + user.getFollowing().size();
        following.setText(followingTitle);

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FollowActivity.class);
                intent.putExtra("USERNAME", user.getUsername());
                intent.putExtra("TYPE", "FOLLOWERS");
                startActivity(intent);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FollowActivity.class);
                intent.putExtra("USERNAME", user.getUsername());
                intent.putExtra("TYPE", "FOLLOWING");
                startActivity(intent);
            }
        });

        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowTask followTask = new FollowTask();
                String loggedUsername = loggedUser.getUsername();
                String targetUsername = user.getUsername();

                if (loggedUser.getFollowing().contains(targetUsername)) {
                    loggedUser.getFollowing().remove(targetUsername);
                    followTask.execute(loggedUsername, targetUsername, "false");
                } else {
                    loggedUser.getFollowing().add(targetUsername);
                    followTask.execute(loggedUsername, targetUsername, "true");
                }
            }
        });
    }

    public void setFollowButtonText() {
        User loggedUser = DataCache.getInstance().getLoggedUser();
        if (loggedUser.getFollowing().contains(user.getUsername())) {
            followButton.setText("Following");
        } else {
            followButton.setText("Follow?");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                userPicture.setImageBitmap(bitmap);
                //TODO: Sloppy fix until photo uploads
                DataCache.getInstance().putPic(user.getPictureURL(), bitmap);
                //TODO: get genuine URL eventually
                PictureTask pictureTask = new PictureTask();
                pictureTask.execute(new PictureRequest(username, "NEW_PICTURE_URL"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class FollowTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            ServerProxy proxy = new ServerProxy();
            boolean state;
            if (strings[2].matches("true")) {
                state = true;
            } else {
                state = false;
            }
            GeneralResult result = proxy.setFollow(new FollowRequest(strings[0], strings[1], state));
            return result.message;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
            setFollowButtonText();
        }
    }

    private class UserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            ServerProxy proxy = new ServerProxy();
            UserResult result = proxy.getUser(new GeneralRequest(strings[0]));
            return result.user;
        }

        @Override
        protected void onPostExecute(User newUser) {
            super.onPostExecute(user);
            user = newUser;
            setValues();
        }
    }

    private class PictureTask extends AsyncTask<PictureRequest, Void, String>{

        @Override
        protected String doInBackground(PictureRequest... pictureRequests) {
            return new ServerProxy().changePicture(pictureRequests[0]).message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
