package com.example.mutualtwo.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Model.Post;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Utility.PatternEditableBuilder;
import com.example.mutualtwo.Utility.ServerProxy;

import java.util.regex.Pattern;

public class PostActivity extends AppCompatActivity {
    Post post;
    ImageView picture;
    Button username;
    TextView name, body;
    ImageView attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        post = DataCache.getInstance().getClickedPost();

        picture = findViewById(R.id.post_profile_picture);
        username = findViewById(R.id.post_username);
        name = findViewById(R.id.post_name);
        body = findViewById(R.id.post_body);
        attachment = findViewById(R.id.post_attachment);

        DataCache.getInstance().setPicture(post.getProfilePictureURL(), picture);
        username.setText(post.getUsername());
        name.setText(post.fullName());
        body.setText(post.getBody());

        String url = post.getAttachment();
        if(url != null){
            DataCache.getInstance().setPicture(url, attachment);
        }
        else{
            attachment.setVisibility(View.GONE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUser(post.getUsername());
            }
        };
        picture.setOnClickListener(onClickListener);
        username.setOnClickListener(onClickListener);

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.MAGENTA,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                new UserTask().execute(text.substring(1));
                            }
                        }).
                addPattern(Pattern.compile("\\#(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getBaseContext(),
                                        "Clicked hashtag: " + text,
                                        Toast.LENGTH_SHORT).show();
                                launchSearch(text.substring(1));
                            }
                        }).into(body);

    }

    private void launchUser(String username){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    private void launchSearch(String hashtag){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("HASHTAG", hashtag);
        startActivity(intent);
    }

    private class UserTask extends AsyncTask<String,Void,User> {

        @Override
        protected User doInBackground(String... strings) {
            return new ServerProxy().getUser(new GeneralRequest(strings[0])).user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null)
                Toast.makeText(getApplicationContext(), "No user found...", Toast.LENGTH_LONG).show();
            else launchUser(user.getUsername());
        }
    }
}
