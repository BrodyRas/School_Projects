package com.example.mutualtwo.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mutualtwo.Requests.GeneralRequest;
import com.example.mutualtwo.Requests.LoginRequest;
import com.example.mutualtwo.Results.AuthResult;
import com.example.mutualtwo.Results.UserResult;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.Utility.ServerProxy;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    Button login_button, register_button;
    EditText username_edit, password_edit;

    public LoginFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        login_button = v.findViewById(R.id.log_login_button);
        register_button = v.findViewById(R.id.log_register_button);
        username_edit = v.findViewById(R.id.log_username_edit);
        password_edit = v.findViewById(R.id.log_password_edit);

        //Login button is only enabled if the EditTexts are populated
        login_button.setEnabled(false);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTask userTask = new UserTask();
                userTask.execute(username_edit.getText().toString());
            }
        });

        TextWatcher editTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableLogInButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        username_edit.addTextChangedListener(editTextWatcher);
        password_edit.addTextChangedListener(editTextWatcher);

        //Switch to registration fragment
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegister();
            }
        });

        return v;
    }

    void enableLogInButton(){
        if(!username_edit.getText().toString().equals("") && (!password_edit.getText().toString().equals(""))){
            login_button.setEnabled(true);
        }
        else{
            login_button.setEnabled(false);
        }
    }

    void attemptLogin(User user){
        if(user == null){
            Toast toast = Toast.makeText(getContext(),
                    "Cannot find user " + username_edit.getText().toString(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            if(!user.getPassword().matches(password_edit.getText().toString())){
                Toast toast = Toast.makeText(getContext(),
                        "User " + username_edit.getText().toString() + " found, but password is incorrect",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                DataCache.getInstance().setLoggedUser(user);
                LogInTask logInTask = new LogInTask();
                logInTask.execute(user.getUsername(), user.getPassword());
            }
        }
    }

    void launchRegister(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.launchRegisterFragment();
    }

    void launchFeed(String username){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.launchFeed(username);
    }

    private class LogInTask extends AsyncTask<String,Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            ServerProxy proxy = new ServerProxy();
            AuthResult result = proxy.logIn(new LoginRequest(strings[0], strings[1]));
            System.out.println(result.token);
            //TODO: datacache.setAuth;
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            launchFeed(s);
        }
    }

    private class UserTask extends AsyncTask<String,Void, User>{

        @Override
        protected User doInBackground(String... strings) {
            ServerProxy proxy = new ServerProxy();
            UserResult result = proxy.getUser(new GeneralRequest(strings[0]));
            return result.user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            attemptLogin(user);
        }
    }
}

