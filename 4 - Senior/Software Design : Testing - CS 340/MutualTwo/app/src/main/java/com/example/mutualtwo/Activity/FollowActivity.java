package com.example.mutualtwo.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mutualtwo.Requests.FollowingRequest;
import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Results.FollowResult;
import com.example.mutualtwo.Results.UserResult;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Utility.ServerProxy;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {
    public static final String TYPE_FOLLOWERS = "FOLLOWERS";
    public static final String TYPE_FOLLOWING = "FOLLOWING";
    ArrayList<String> follows;
    TextView usernameText, typeText;
    User myUser;
    ArrayList<User> followUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        String username = getIntent().getStringExtra("USERNAME");

        MainUserTask userTask = new MainUserTask();
        userTask.execute(username);
    }

    private void initRecyclerView() {
        String type = getIntent().getStringExtra("TYPE");

        usernameText = findViewById(R.id.follow_username);
        typeText = findViewById(R.id.follow_type);

        usernameText.setText(myUser.getUsername());
        typeText.setText(type.toLowerCase());

        RecyclerView recyclerView = findViewById(R.id.follow_recycler);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(follows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        ArrayList<String> follows;

        public RecyclerViewAdapter(ArrayList<String> follows) {
            this.follows = follows;
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.user_layout, viewGroup, false);
            return new ViewHolder(view);
        }

        //Set values of views
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final User user = followUsers.get(i);

            DataCache.getInstance().setPicture(user.getPictureURL(), viewHolder.profilePicture);

            viewHolder.username.setText(user.getUsername());
            viewHolder.name.setText(user.fullName());

            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchUser(user.getUsername());
                }
            });
        }

        @Override
        public int getItemCount() {
            return follows.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout layout;
            ImageView profilePicture;
            TextView username, name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.follow_layout);
                profilePicture = itemView.findViewById(R.id.follow_profile_picture);
                username = itemView.findViewById(R.id.follow_username);
                name = itemView.findViewById(R.id.follow_name);
            }
        }
    }

    private void launchUser(String username){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    private class MainUserTask extends AsyncTask<String,Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            ServerProxy proxy = new ServerProxy();
            UserResult result = proxy.getUser(new GeneralRequest(strings[0]));
            return result.user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            myUser = user;
            new FollowUserTask().execute(myUser.getUsername());
        }
    }

    private class FollowUserTask extends AsyncTask<String,Void, FollowResult> {

        @Override
        protected FollowResult doInBackground(String... strings) {
            String type = getIntent().getStringExtra("TYPE");

            switch (type){
                case TYPE_FOLLOWERS:
                    follows = new ArrayList<>(myUser.getFollowers());
                    break;
                case TYPE_FOLLOWING:
                    follows = new ArrayList<>(myUser.getFollowing());
                    break;
            }

            ServerProxy proxy = new ServerProxy();
            FollowResult result = proxy.getFollows(new FollowingRequest(strings[0], type));
            return result;
        }

        @Override
        protected void onPostExecute(FollowResult followResult) {
            super.onPostExecute(followResult);
            followUsers = followResult.users;

            initRecyclerView();
        }
    }
}
