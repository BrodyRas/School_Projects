package Results;

import java.util.Objects;

public class RegisterResult {
    public RegisterResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.message = null;
    }

    public RegisterResult(String message) {
        this.authToken = null;
        this.userName = null;
        this.personID = null;
        this.message = message;
    }

    String authToken;
    String userName;
    String personID;
    String message;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterResult)) return false;
        RegisterResult that = (RegisterResult) o;
        return Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getPersonID(), that.getPersonID()) &&
                Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken(), getPersonID(), getMessage());
    }
}
