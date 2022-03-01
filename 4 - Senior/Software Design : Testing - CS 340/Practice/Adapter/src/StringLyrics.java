import java.util.ArrayList;
import java.util.List;

public class StringLyrics implements StringSource {
    private List<String> strings;
    private int i = 0;
    public StringLyrics(){
        strings = new ArrayList<>();
        strings.add("I");
        strings.add("like");
        strings.add("big");
        strings.add("butts");
    }

    @Override
    public String next() {
        return strings.get(i++);
    }
}
