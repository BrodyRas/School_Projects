package Factory;

import HighLevel.DictionaryInterface;

import java.io.IOException;

public interface iDictionaryFactory {
    DictionaryInterface getDictionary() throws IOException;
}
