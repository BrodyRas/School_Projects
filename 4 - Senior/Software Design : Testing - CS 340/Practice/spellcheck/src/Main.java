import Factory.ExtractorFactory;
import Factory.FetcherFactory;
import HighLevel.SpellingChecker;
import Factory.DictionaryFactory;

import java.io.*;
import java.net.*;
import java.util.*;


public class Main {
	public static void main(String[] args) {
		try {
			URL url = new URL(args[0]);

			DictionaryFactory dictionaryFactory = new DictionaryFactory();
			FetcherFactory fetcherFactory = new FetcherFactory();
			ExtractorFactory extractorFactory = new ExtractorFactory();

			SpellingChecker checker = new SpellingChecker(
												dictionaryFactory.getDictionary(),
												fetcherFactory.getFetcher("URL"),
												extractorFactory.getExtractor());

			SortedMap<String, Integer> mistakes = checker.check(url);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			System.out.println(e);
		}

		test(args[0]);
	}

	public static void test(String fileName){
		try {
			URL url = new URL(fileName);
			DictionaryFactory dictionaryFactory = new DictionaryFactory();
			FetcherFactory fetcherFactory = new FetcherFactory();
			ExtractorFactory extractorFactory = new ExtractorFactory();

			SpellingChecker checker = new SpellingChecker(
												dictionaryFactory.getDictionary(),
												fetcherFactory.getFetcher("BOGUS"),
												extractorFactory.getExtractor());

			SortedMap<String, Integer> mistakes = checker.check(url);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}

