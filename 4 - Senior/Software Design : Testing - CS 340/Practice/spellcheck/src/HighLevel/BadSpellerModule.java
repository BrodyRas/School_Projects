package HighLevel;

import LowLevel.BogusFetcher;
import LowLevel.Dictionary;
import LowLevel.URLFetcher;
import LowLevel.WordExtractor;
import com.google.inject.AbstractModule;

public class BadSpellerModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(DictionaryInterface.class).to(Dictionary.class);
        bind(URLFetcherInterface.class).to(BogusFetcher.class);
        bind(WordExtractorInterface.class).to(WordExtractor.class);
    }
}
