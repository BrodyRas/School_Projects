package Result;

public class AuthResult {
    public AuthResult(){}
    public AuthResult(String username, String token) {
        this.username = username;
        this.token = token;
    }
    public AuthResult(String message) {
        this.message = message;
    }

    public String message;
    public String username;
    public String token;
}
