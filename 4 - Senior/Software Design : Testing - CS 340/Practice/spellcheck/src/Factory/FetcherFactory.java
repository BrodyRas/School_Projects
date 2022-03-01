package Factory;

import HighLevel.URLFetcherInterface;
import LowLevel.BogusFetcher;
import LowLevel.URLFetcher;

public class FetcherFactory implements iFetcherFactory {
    @Override
    public URLFetcherInterface getFetcher(String type) {
        switch (type){
            case "URL":
                return new URLFetcher();
            case "BOGUS":
                return new BogusFetcher();
        }
        return null;
    }
}
