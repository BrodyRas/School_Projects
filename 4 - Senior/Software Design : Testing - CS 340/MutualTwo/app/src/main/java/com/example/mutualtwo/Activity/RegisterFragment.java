package com.example.mutualtwo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mutualtwo.Requests.LoginRequest;
import com.example.mutualtwo.Requests.UserRequest;
import com.example.mutualtwo.Results.AuthResult;
import com.example.mutualtwo.Utility.DataCache;
import com.example.mutualtwo.Model.Name;
import com.example.mutualtwo.R;
import com.example.mutualtwo.Model.User;
import com.example.mutualtwo.Utility.ServerProxy;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterFragment extends Fragment {
    public static final int GET_FROM_GALLERY = 3;
    Button picture_button, login_button, register_button;
    EditText first_edit, last_edit, username_edit, password_edit;
    ImageView picture_check;
    Bitmap bitmap;


    public RegisterFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        first_edit = v.findViewById(R.id.reg_first_name_edit);
        last_edit = v.findViewById(R.id.reg_last_name_edit);
        username_edit = v.findViewById(R.id.reg_username_edit);
        password_edit = v.findViewById(R.id.reg_password_edit);

        picture_button = v.findViewById(R.id.reg_picture_button);
        picture_check = v.findViewById(R.id.reg_picture_checker);

        register_button = v.findViewById(R.id.reg_register_button);
        login_button = v.findViewById(R.id.reg_login_button);

        //Simulate register
        register_button.setEnabled(false);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(first_edit.getText().toString(),
                        last_edit.getText().toString(),
                        username_edit.getText().toString(),
                        password_edit.getText().toString(),
                        "fix this eventually");    //TODO: Please do

                //TODO: This is just until I start actually uploading photos, and getting URLS
                DataCache.getInstance().putPic(user.getPictureURL(), bitmap);
                DataCache.getInstance().setLoggedUser(user);

                RegisterTask registerTask = new RegisterTask();
                registerTask.execute(user);
            }
        });

        //Register button is only enabled if the EditTexts are populated, and a valid picture is found
        TextWatcher editTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        first_edit.addTextChangedListener(editTextWatcher);
        last_edit.addTextChangedListener(editTextWatcher);
        username_edit.addTextChangedListener(editTextWatcher);
        password_edit.addTextChangedListener(editTextWatcher);

        //Return to login
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLogIn();
            }
        });
        picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
            }
        });

        return v;
    }

    void enableRegisterButton(){
        if(!first_edit.getText().toString().equals("")
                && (!last_edit.getText().toString().equals(""))
                && (!username_edit.getText().toString().equals(""))
                && (!password_edit.getText().toString().equals(""))
                && (picture_check.getDrawable() != null)){
            register_button.setEnabled(true);
        }
        else{
            register_button.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                picture_check.setImageBitmap(bitmap);
                enableRegisterButton();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Cancel registration, return to login
    void launchLogIn(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.launchLoginFragment();
    }

    void launchFeed(String username){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.launchFeed(username);
    }

    private class RegisterTask extends AsyncTask<User,Void, String> {
        @Override
        protected String doInBackground(User... users) {
            ServerProxy proxy = new ServerProxy();
            AuthResult result = proxy.register(new UserRequest(users[0]));

            //TODO: fix this
            System.out.println(result.message);
            //TODO: datacache.setAuth;
            return users[0].getUsername();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            launchFeed(s);
        }
    }
}