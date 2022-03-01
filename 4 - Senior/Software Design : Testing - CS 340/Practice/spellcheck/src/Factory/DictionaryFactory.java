package Factory;

import HighLevel.DictionaryInterface;
import LowLevel.Dictionary;

import java.io.IOException;

public class DictionaryFactory implements iDictionaryFactory {
    @Override
    public DictionaryInterface getDictionary() throws IOException {
        return new Dictionary();
    }
}
