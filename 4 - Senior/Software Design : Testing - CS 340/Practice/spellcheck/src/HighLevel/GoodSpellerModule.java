package HighLevel;

import LowLevel.Dictionary;
import LowLevel.URLFetcher;
import LowLevel.WordExtractor;
import com.google.inject.AbstractModule;

public class GoodSpellerModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(DictionaryInterface.class).to(Dictionary.class);
        bind(URLFetcherInterface.class).to(URLFetcher.class);
        bind(WordExtractorInterface.class).to(WordExtractor.class);
    }
}
