package Requests;

public class LoginRequest {
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    String userName;
    String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
