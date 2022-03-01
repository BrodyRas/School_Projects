package com.example.familymapclient.Activities.Main;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.Models.Client;
import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import Models.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.GeneralResult;
import Results.LoginResult;
import Results.RegisterResult;
import Utility.NameList;

public class LoginFragment extends Fragment {
    private static final String TAG = "LOGINFRAG";
    private Button logInButton, registerButton, clearButton, randomButton;
    private String serverHost, serverPort;
    private EditText sh, sp, un, pw, fn, ln, em;
    private RadioGroup gb;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,
                container,
                false);

        //Get references to all the interactive elements of the view
        logInButton = v.findViewById(R.id.log_in_button);
        registerButton = v.findViewById(R.id.register_button);
        randomButton = v.findViewById(R.id.random_register_button);
        clearButton = v.findViewById(R.id.clear_db_button);
        sh = v.findViewById(R.id.server_host_entry);
        sp = v.findViewById(R.id.server_port_entry);
        un = v.findViewById(R.id.username_entry);
        pw = v.findViewById(R.id.password_entry);
        fn = v.findViewById(R.id.first_name_entry);
        ln = v.findViewById(R.id.last_name_entry);
        em = v.findViewById(R.id.email_entry);
        gb = v.findViewById(R.id.gender_buttons);

        //until essential fields are filled, deactivate buttons
        logInButton.setEnabled(false);
        registerButton.setEnabled(false);

        //Constantly check for inputs, to see if the buttons can be activated yet
        EditWatcher editWatcher = new EditWatcher();
        RadioWatcher radioWatcher = new RadioWatcher();
        un.addTextChangedListener(editWatcher);
        pw.addTextChangedListener(editWatcher);
        fn.addTextChangedListener(editWatcher);
        ln.addTextChangedListener(editWatcher);
        em.addTextChangedListener(editWatcher);
        gb.setOnCheckedChangeListener(radioWatcher);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the current values of necessary fields
                Log.d(TAG, "logIn clicked");
                serverHost = sh.getText().toString();
                serverPort = sp.getText().toString();
                String username = un.getText().toString();
                String password = pw.getText().toString();

                //Create request from acquired data, and begin task
                LoginRequest loginRequest = new LoginRequest(username, password);
                LogInTask logInTask = new LogInTask(getContext());
                logInTask.execute(loginRequest);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "register clicked");
                if (container != null) {
                    //get the current values of necessary fields
                    serverHost = sh.getText().toString();
                    serverPort = sp.getText().toString();
                    String username = un.getText().toString();
                    String password = pw.getText().toString();
                    String firstName = fn.getText().toString();
                    String lastName = ln.getText().toString();
                    String email = em.getText().toString();
                    String gender;
                    if (gb.getCheckedRadioButtonId() == R.id.maleButton) gender = "m";
                    else gender = "f";

                    //Create request from acquired data, and begin task
                    User user = new User(username, password, email,
                            firstName, lastName, gender, null);
                    RegisterRequest registerRequest = new RegisterRequest(user);
                    RegisterTask registerTask = new RegisterTask(getContext());
                    registerTask.execute(registerRequest);
                }
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "random clicked");
                generateRandomEntry();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clear clicked");
                //Clear database
                serverHost = sh.getText().toString();
                serverPort = sp.getText().toString();
                ClearTask clearTask = new ClearTask(getContext());
                clearTask.execute();
            }
        });

        return v;
    }

    private void generateRandomEntry() {
        NameList maleNames = null;
        NameList surNames = null;
        try {
            Gson gson = new Gson();
            AssetManager assetManager = getContext().getAssets();
            InputStream in = assetManager.open("mnames.json");
            maleNames = gson.fromJson(new InputStreamReader(in), NameList.class);
            in = assetManager.open("snames.json");
            surNames = gson.fromJson(new InputStreamReader(in), NameList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String firstName = maleNames.random();
        String lastName = surNames.random();
        String email = firstName + "@" + lastName + ".com";
        String username = firstName + lastName;
        Random rand = new Random();
        String password = lastName + (rand.nextInt((999 - 100) + 1) + 100); //produces a 3 digit number

        un.setText(username);
        pw.setText(password);
        fn.setText(firstName);
        ln.setText(lastName);
        em.setText(email);
        gb.check(R.id.maleButton);
    }

    //Makes sure the LogIn and Register buttons are only active if the necessary fields are filled
    private void activateButtons() {
        //LogIn Button logic
        if (!un.getText().toString().matches("") &&
                !pw.getText().toString().matches("")) {
            logInButton.setEnabled(true);
        } else {
            logInButton.setEnabled(false);
        }

        //Register Button logic
        if (!un.getText().toString().matches("") &&
                !pw.getText().toString().matches("") &&
                !fn.getText().toString().matches("") &&
                !ln.getText().toString().matches("") &&
                !em.getText().toString().matches("") &&
                gb.getCheckedRadioButtonId() != -1) {
            registerButton.setEnabled(true);
        } else {
            registerButton.setEnabled(false);
        }
    }

    //whenever the EditTexts or Radio Buttons are changed, these classes call activateButtons
    public class EditWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        //the only one that matters
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            activateButtons();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class RadioWatcher implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            activateButtons();
        }
    }

    //AsyncTask's created to handle server exchanges in the background
    public class RegisterTask extends AsyncTask<RegisterRequest, Integer, RegisterResult> {
        Context mContext = null;

        public RegisterTask(Context context) {
            mContext = context;

        }

        //Call the client, which sends the HTTP request to the server
        @Override
        protected RegisterResult doInBackground(RegisterRequest... registerRequests) {
            Client client = new Client(serverHost, serverPort);
            return client.register(registerRequests[0]);
        }

        @Override
        protected void onPostExecute(RegisterResult registerResult) {
            super.onPostExecute(registerResult);
            if (registerResult.getMessage() == null) {
                CacheTask cacheTask = new CacheTask(mContext);
                cacheTask.execute(registerResult);
            } else {
                Toast toast = Toast.makeText(mContext, "Register failed! :/", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public class LogInTask extends AsyncTask<LoginRequest, Integer, LoginResult> {
        Context mContext = null;

        public LogInTask(Context context) {
            mContext = context;
        }

        //Call the client, which sends the HTTP request to the server
        @Override
        protected LoginResult doInBackground(LoginRequest... loginRequests) {
            Client client = new Client(serverHost, serverPort);
            return client.logIn(loginRequests[0]);
        }

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            super.onPostExecute(loginResult);
            if (loginResult.getMessage() == null) {
                CacheTask cacheTask = new CacheTask(mContext);
                //Cast the LoginResult into a RegisterResult (what it accepts)
                RegisterResult registerResult = new RegisterResult(loginResult.getAuthToken(),
                        loginResult.getUserName(),
                        loginResult.getPersonID());
                cacheTask.execute(registerResult);
            } else {
                Toast toast = Toast.makeText(mContext, "Log in failed! :/", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public class ClearTask extends AsyncTask<Void, Void, GeneralResult> {
        Context mContext = null;

        public ClearTask(Context context) {
            mContext = context;
        }

        @Override
        protected GeneralResult doInBackground(Void... voids) {
            Client client = new Client(serverHost, serverPort);
            return client.clear();
        }

        @Override
        protected void onPostExecute(GeneralResult generalResult) {
            super.onPostExecute(generalResult);
            Toast toast = Toast.makeText(mContext, generalResult.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public class CacheTask extends AsyncTask<RegisterResult, Void, Void> {
        Context mContext = null;

        public CacheTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(RegisterResult... registerResults) {
            //Get user data from server
            Client client = new Client(serverHost, serverPort);
            client.getCache(registerResults[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void hello) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.launchMapFragment();
        }
    }

}
