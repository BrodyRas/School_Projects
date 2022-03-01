package Factory;

import HighLevel.URLFetcherInterface;

public interface iFetcherFactory {
    URLFetcherInterface getFetcher(String type);
}
