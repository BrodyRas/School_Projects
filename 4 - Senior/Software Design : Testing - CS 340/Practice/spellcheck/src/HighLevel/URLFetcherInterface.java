package HighLevel;

import java.io.IOException;
import java.net.URL;

public interface URLFetcherInterface {
    String fetch(URL url) throws IOException;
}
