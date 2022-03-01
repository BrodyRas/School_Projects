package Models;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

public class AuthToken {
    public AuthToken(String username) {
        this.userName = username;
        token = RandomStringUtils.random(12, "1234567890abcdef");
    }

    public AuthToken(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    private String userName, token;

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken)) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(getUserName(), authToken.getUserName()) &&
                Objects.equals(getToken(), authToken.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getToken());
    }
}
