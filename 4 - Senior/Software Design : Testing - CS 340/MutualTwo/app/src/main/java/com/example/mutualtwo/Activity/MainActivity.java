package com.example.mutualtwo.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mutualtwo.R;

public class MainActivity extends AppCompatActivity {
    public static final int LOGGING_OUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_frame);

        if(fragment == null){
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.main_frame, fragment).commit();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == LOGGING_OUT  && resultCode  == RESULT_OK) {
                assert data != null;
                String result = data.getStringExtra("TYPE");
                if(result.matches("LOGOUT")){
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Fragment fragment = new LoginFragment();
                    fragmentTransaction.replace(R.id.main_frame, fragment).commit();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Upon successfully logging in/registering, move from login fragment to map fragment
    public void launchRegisterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragment = new RegisterFragment();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();
    }

    public void launchLoginFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragment = new LoginFragment();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();
    }

    public void launchFeed(String username){
        //some login database stuff
        Intent intent = new Intent(this, FeedActivity.class);
        intent.putExtra("USERNAME", username);
        startActivityForResult(intent, LOGGING_OUT);
    }
}
