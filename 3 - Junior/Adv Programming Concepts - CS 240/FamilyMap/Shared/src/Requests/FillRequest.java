package Requests;

public class FillRequest {
    public FillRequest(String username, int generationCount) {
        this.username = username;
        this.generationCount = generationCount;
    }

    int generationCount;
    String username;

    public String getUsername() {
        return username;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGenerationCount(int generationCount) {
        this.generationCount = generationCount;
    }
}
