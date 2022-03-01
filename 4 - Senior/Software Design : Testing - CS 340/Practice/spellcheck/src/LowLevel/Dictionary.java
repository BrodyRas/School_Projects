package LowLevel;

import HighLevel.DictionaryInterface;

import java.io.*;
import java.util.*;


public class Dictionary implements DictionaryInterface {
	private Set<String> words;
	private String fileName = "dict.txt";

	public Dictionary() throws IOException{
		scan(fileName);
	}

	public void scan(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		try {
			words = new TreeSet<String>();
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine().trim();
				words.add(word);
			}
		}
		finally {
			scanner.close();
		}
	}

	public boolean isValidWord(String word) {
		return words.contains(word);
	}

}

