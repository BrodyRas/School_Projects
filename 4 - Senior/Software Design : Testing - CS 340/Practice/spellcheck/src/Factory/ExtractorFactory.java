package Factory;

import HighLevel.WordExtractorInterface;
import LowLevel.WordExtractor;

public class ExtractorFactory implements iExtractorFactory {
    @Override
    public WordExtractorInterface getExtractor() {
        return new WordExtractor();
    }
}
