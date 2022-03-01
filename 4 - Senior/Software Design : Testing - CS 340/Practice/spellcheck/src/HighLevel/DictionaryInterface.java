package HighLevel;

import java.io.FileNotFoundException;

public interface DictionaryInterface {
    boolean isValidWord(String word);
    void scan(String fileName) throws FileNotFoundException;
}
