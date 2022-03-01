package Model;

import java.security.SecureRandom;
import java.util.Arrays;

public class AuthToken {
    public AuthToken(String username) {
        this.username = username;
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        token = Arrays.toString(bytes);
    }

    String username, token;

    public String getUsername() {
        return username;
    }
    public String getToken() {
        return token;
    }
}
