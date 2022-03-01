package com.example.mutualtwo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Utility.ServerProxy;

public class SearchActivity extends AppCompatActivity {
    EditText searchEdit;
    Button userButton, hashButton;
    String username, hashtag;
    User searchedUser;

    LinearLayout userHolder;
    ImageView profilePic;
    TextView usernameText, nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        hashtag = getIntent().getStringExtra("HASHTAG");
        if(hashtag != null){ populateHashtags(); }

        searchEdit = findViewById(R.id.search_edit);
        userButton = findViewById(R.id.search_user);
        hashButton = findViewById(R.id.search_hash);

        //Used to populate user if found
        userHolder = findViewById(R.id.search_user_holder);
        profilePic = findViewById(R.id.search_profile_picture);
        usernameText = findViewById(R.id.search_username);
        nameText = findViewById(R.id.search_name);

        userHolder.setVisibility(View.GONE);

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedUser = null;
                findViewById(R.id.search_frame).setVisibility(View.GONE);
                UserTask userTask = new UserTask();
                userTask.execute(searchEdit.getText().toString());
            }
        });

        hashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userHolder.setVisibility(View.GONE);
                findViewById(R.id.search_frame).setVisibility(View.VISIBLE);
                hashtag = searchEdit.getText().toString();
                hideSoftKeyboard();
                populateHashtags();
            }
        });

        populateHashtags();
    }

    private void populateHashtags(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.search_frame);
        Bundle args = new Bundle();
        args.putString("USERNAME", null);
        args.putString("TYPE", hashtag);

        if(fragment == null){
            fragment = new FeedFragment();
            fragment.setArguments(args);
            fm.beginTransaction().add(R.id.search_frame, fragment).commit();
        }
        else{
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragment = new FeedFragment();
            fragment.setArguments(args);
            fragmentTransaction.replace(R.id.search_frame, fragment).commit();
        }
    }

    private void populateUserSearch(){
        if(searchedUser != null){
            DataCache.getInstance().setPicture(searchedUser.getPictureURL(), profilePic);
            usernameText.setText(searchedUser.getUsername());
            nameText.setText(searchedUser.fullName());
            userHolder.setVisibility(View.VISIBLE);
            userHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchUser(searchedUser.getUsername());
                }
            });
        }
        else{
            userHolder.setVisibility(View.GONE);
        }
        hideSoftKeyboard();
    }

    private void launchUser(String username){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
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
            else searchedUser = user;

            populateUserSearch();
        }
    }
}
