package LowLevel;

import HighLevel.URLFetcherInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class BogusFetcher implements URLFetcherInterface {
	public String fetch(URL url) {
		return "something dumm";
	}
}

