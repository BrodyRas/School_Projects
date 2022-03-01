package com.example.mutualtwo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Requests.PictureRequest;
import com.example.mutualtwo.Requests.PostRequest;
import com.example.mutualtwo.Results.GeneralResult;
import com.example.mutualtwo.Results.UserResult;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Model.Post;
import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Utility.ServerProxy;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.mutualtwo.Activity.RegisterFragment.GET_FROM_GALLERY;

public class FeedActivity extends AppCompatActivity {
    User user;
    ImageView profilePicture, attachment;
    TextView username;
    EditText postEntry;
    Button attachButton, postButton, yourProfileButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        user = DataCache.getInstance().getLoggedUser();

        //Get references
        profilePicture = findViewById(R.id.feed_picture);
        username = findViewById(R.id.feed_username);
        postEntry = findViewById(R.id.feed_post_body);
        attachButton = findViewById(R.id.feed_attach_button);
        attachment = findViewById(R.id.feed_post_attachment);
        postButton = findViewById(R.id.feed_post_button);
        yourProfileButton = findViewById(R.id.feed_own_profile_button);
        logoutButton = findViewById(R.id.feed_logout_button);

        //attach onClickListeners
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = postEntry.getText().toString();
                if(!text.matches("")){
                    //Get the attachment
                    BitmapDrawable drawable = (BitmapDrawable) attachment.getDrawable();
                    //TODO: Delete this
                    String DELETE_THIS = null;
                    if(drawable != null){
                        Bitmap bitmap = drawable.getBitmap();
                        DELETE_THIS = "fixit";
                        DataCache.getInstance().putPic(DELETE_THIS, bitmap);
                        attachment.setImageDrawable(null);
                    }
                    //Post post = new Post(user, text, DELETE_THIS);
                    postEntry.setText("");
                    hideSoftKeyboard();
                    MakePostTask makePostTask = new MakePostTask();
                    makePostTask.execute(new PostRequest(user.getUsername(), text, DELETE_THIS));
                }
            }
        });

        yourProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUser();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().setLoggedUser(null);
                DataCache.getInstance().clearCache();

                Intent intent = getIntent();
                intent.putExtra("TYPE", "LOGOUT");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        populateHeader();
        populateFeed();
    }

    public void populateHeader(){
        //populate the user post prompt
        if(user != null){
            DataCache.getInstance().setPicture(user.getPictureURL(), profilePicture);
            username.setText(user.getFirstName());
        }
        else{
            Toast toast = Toast.makeText(this, "Invalid user passed", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Initially populate the feed, or refresh upon creating a new post
    public void populateFeed(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.feed_frame);
        if(fragment == null){
            fragment = new FeedFragment();
            Bundle args = new Bundle();
            args.putString("USERNAME", user.getUsername());
            args.putString("TYPE", "FEED");
            fragment.setArguments(args);
            fm.beginTransaction().add(R.id.feed_frame, fragment).commit();
        }
        else{
            fragment = new FeedFragment();
            Bundle args = new Bundle();
            args.putString("USERNAME", user.getUsername());
            args.putString("TYPE", "FEED");
            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.feed_frame, fragment).commit();
        }
    }

    //Accesses the Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                attachment.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchUser(){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("USERNAME", DataCache.getInstance().getLoggedUser().getUsername());
        startActivity(intent);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
    }

    private class MakePostTask extends AsyncTask<PostRequest, Void, GeneralResult>{
        @Override
        protected GeneralResult doInBackground(PostRequest... requests) {
            ServerProxy proxy = new ServerProxy();
            return proxy.makePost(requests[0]);
        }

        @Override
        protected void onPostExecute(GeneralResult result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_LONG);
            populateHeader();
            populateFeed();
        }
    }
}