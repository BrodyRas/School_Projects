package HighLevel;

import LowLevel.URLFetcher;
import LowLevel.WordExtractor;
import LowLevel.Dictionary;
import com.google.inject.Inject;

import java.util.*;
import java.net.*;
import java.io.*;


public class SpellingChecker {
	DictionaryInterface dictionary;
	URLFetcherInterface urlFetcher;
	WordExtractorInterface wordExtractor;

	@Inject
	public SpellingChecker(DictionaryInterface dictionary, URLFetcherInterface urlFetcher, WordExtractorInterface wordExtractor) {
		this.dictionary = dictionary;
		this.urlFetcher = urlFetcher;
		this.wordExtractor = wordExtractor;
	}

	public SortedMap<String, Integer> check(URL url) throws IOException {

		// download the document content
		//
		String content = urlFetcher.fetch(url);

		// extract words from the content
		//
		List<String> words = wordExtractor.extract(content);

		// find spelling mistakes
		//
		SortedMap<String, Integer> mistakes = new TreeMap<String, Integer>();
		
		Iterator<String> it = words.iterator();
		while (it.hasNext()) {
			String word = it.next();
			if (!dictionary.isValidWord(word)) {
				if (mistakes.containsKey(word)) {
					int oldCount = mistakes.get(word);
					mistakes.put(word, (oldCount + 1));
				}
				else {
					mistakes.put(word, 1);
				}
			}
		}

		return mistakes;
	}

	public DictionaryInterface getDictionary() {
		return dictionary;
	}
}

