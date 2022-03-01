package com.example.familymapclient.Models;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.GeneralResult;
import Results.LoginResult;
import Results.RegisterResult;

public class Client {
    private String serverHost, serverPort;

    public Client(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    //Sends RegisterRequest to server
    public RegisterResult register(RegisterRequest registerRequest) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            //Maybe create a User class w/o a personID?
            Gson gson = new Gson();
            User user = registerRequest.getUser();
            MiniUser mini = new MiniUser(user); //used to create a JSON w/o the nul PersonID column
            String reqData = gson.toJson(mini);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("New user successfully registered!");
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, RegisterResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Sends LoginRequest to server
    public LoginResult logIn(LoginRequest loginRequest) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(loginRequest);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully logged in!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, LoginResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Clears the database
    public GeneralResult clear() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Database successfully cleared!");

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }

            Gson gson = new Gson();
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            return gson.fromJson(resultText, GeneralResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get the user's data from the server
    public void getCache(RegisterResult registerResult) {
        DataCache.getInstance().setServerHost(serverHost);
        DataCache.getInstance().setServerPort(serverPort);
        DataCache.getInstance().setAuthToken(registerResult.getAuthToken());

        //GET USER
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/" + registerResult.getUserName());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Authorization", registerResult.getAuthToken());
            http.setDoOutput(false);
            http.connect();

            Gson gson = new Gson();
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            try {
                UserHolder user = (UserHolder) gson.fromJson(resultText, UserHolder.class);
                DataCache.getInstance().setUser(user.getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //GET PERSONS
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Authorization", registerResult.getAuthToken());
            http.setDoOutput(false);
            http.connect();

            Gson gson = new Gson();
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            PersonList personList = null;
            try {
                personList = (PersonList) gson.fromJson(resultText, PersonList.class);
                DataCache.getInstance().setPersons(personList.getPersons());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //GET EVENTS
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Authorization", registerResult.getAuthToken());
            http.setDoOutput(false);
            http.connect();

            Gson gson = new Gson();
            InputStream resBody = http.getInputStream();
            String resultText = readString(resBody);
            EventList eventList = null;
            try {
                eventList = (EventList) gson.fromJson(resultText, EventList.class);
                DataCache.getInstance().setEvents(eventList.getEvents());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Used to generate a RegisterRequest (Json is identical to a user, besides lacking a PersonID)
    private class MiniUser {
        private String userName;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String gender;

        public MiniUser(User user) {
            this.userName = user.getUserName();
            this.password = user.getPassword();
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.gender = user.getGender();
        }
    }

    //Used to deserialize Json arrays
    public class PersonList {
        private Person[] persons;

        public Person[] getPersons() {
            return persons;
        }
    }

    public class EventList {
        private Event[] events;

        public Event[] getEvents() {
            return events;
        }
    }

    private class UserHolder {
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    //Convenience functions used to access Request/Response bodies
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}