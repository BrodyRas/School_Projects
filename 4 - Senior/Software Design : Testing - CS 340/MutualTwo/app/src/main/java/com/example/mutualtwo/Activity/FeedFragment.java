package com.example.mutualtwo.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Results.PostListResult;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Model.Post;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Utility.PatternEditableBuilder;
import com.example.mutualtwo.Utility.ServerProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;


public class FeedFragment extends Fragment {
    ArrayList<Post> posts = new ArrayList<>();
    PostListResult result;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_feed, container, false);

        //Is this a user's story, a user's feed, or a hashtag search?
        String username = getArguments().getString("USERNAME");
        String type = getArguments().getString("TYPE");
        GetPostsTask getPostsTask = new GetPostsTask();
        getPostsTask.execute(username,type);

        return v;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = v.findViewById(R.id.feed_recycler);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(posts, getContext());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private ArrayList<Post> posts;
        private Context context;

        public RecyclerViewAdapter(ArrayList<Post> posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.post_layout, viewGroup, false);
            return new ViewHolder(view);
        }

        //Set the values of the view
        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.username.setText(posts.get(i).getUsername());
            viewHolder.name.setText(posts.get(i).getFirstName());
            viewHolder.body.setText(posts.get(i).getBody());
            DataCache.getInstance().setPicture(posts.get(i).getProfilePictureURL(), viewHolder.profilePicture);
            viewHolder.time.setText(posts.get(i).getTimeString());

            ClickListener userClickListener = new RecyclerViewAdapter.ClickListener(posts.get(i), "user");
            ClickListener postClickListener = new RecyclerViewAdapter.ClickListener(posts.get(i), "post");
            viewHolder.profilePicture.setOnClickListener(userClickListener);
            viewHolder.username.setOnClickListener(userClickListener);
            viewHolder.layout.setOnClickListener(postClickListener);
            viewHolder.body.setOnClickListener(postClickListener);
            viewHolder.attachment.setOnClickListener(postClickListener);

            String url = posts.get(i).getAttachment();
            if (url == null) {
                viewHolder.attachment.setVisibility(View.GONE);
            } else {
                DataCache.getInstance().setPicture(url, viewHolder.attachment);
            }

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
                                    Toast.makeText(context, "Clicked hashtag: " + text,
                                            Toast.LENGTH_SHORT).show();
                                    launchSearch(text.substring(1));
                                }
                            }).into(viewHolder.body);
        }

        //Set behavior for what'll happen onClick
        private class ClickListener implements View.OnClickListener {
            Post post;
            String type;

            public ClickListener(Post post, String type) {
                this.post = post;
                this.type = type;
            }

            @Override
            public void onClick(View v) {
                switch (type) {
                    case "user":
                        String username = post.getUsername();
                        launchUser(username);
                        break;
                    case "post":
                        launchPost(post);
                }
            }
        }

        //Get total size
        @Override
        public int getItemCount() {
            return posts.size();
        }

        //Get references to the elements of the post
        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout layout;
            ImageView profilePicture;
            Button username;
            TextView name;
            TextView body;
            ImageView attachment;
            TextView time;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.post_layout);
                profilePicture = itemView.findViewById(R.id.post_profile_picture);
                username = itemView.findViewById(R.id.post_username);
                name = itemView.findViewById(R.id.post_name);
                body = itemView.findViewById(R.id.post_body);
                attachment = itemView.findViewById(R.id.post_attachment);
                time = itemView.findViewById(R.id.post_time);
            }
        }
    }

    private void launchUser(String username) {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    private void launchPost(Post post) {
        Intent intent = new Intent(getContext(), PostActivity.class);
        DataCache.getInstance().setClickedPost(post);
        startActivity(intent);
    }

    private void launchSearch(String hashtag) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra("HASHTAG", hashtag);
        startActivity(intent);
    }

    private class GetPostsTask extends AsyncTask<String,Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String username = strings[0];
            String type = strings[1];
            ServerProxy proxy = new ServerProxy();

            if(username != null){
                switch (type){
                    case "STORY":
                        result = proxy.getStory(new GeneralRequest(username));
                        break;
                    case "FEED":
                        result = proxy.getFeed(new GeneralRequest(username));
                        break;
                }
            }
            else{
                result = proxy.getHashtaggedPosts(new GeneralRequest(type));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            posts = result.posts;
            if(posts != null)for(Post p : posts) p.parseTime();
            Collections.sort(posts, Collections.<Post>reverseOrder());
            initRecyclerView();
        }
    }

    private class UserTask extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return new ServerProxy().getUser(new GeneralRequest(strings[0])).user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null)
                Toast.makeText(getContext(), "No user found...", Toast.LENGTH_LONG).show();
            else launchUser(user.getUsername());
        }
    }
}
