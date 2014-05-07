import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) {
		try {
			// stop words
			BufferedReader br1 = new BufferedReader(new FileReader(new File(
					"stopwords\\" + args[1])));

			// text
			BufferedReader br2 = new BufferedReader(new FileReader(new File(
					args[0])));

			// read stop words
			ArrayList<String> stopwords = new ArrayList<String>();
			String line = br1.readLine();
			while (line != null) {
				stopwords.add(line);
				line = br1.readLine();
			}
			System.out.println(stopwords);

			// count of all words
			int wordsCount = 0;

			// read text and create frequency map
			HashMap<String, Integer> words = new HashMap<String, Integer>();
			line = br2.readLine();
			while (line != null) {
				String[] split = line.toLowerCase().replaceAll("\\.", "")
						.replaceAll("\\[", "").replaceAll("\\]", "").split(" ");
				for (String word : split) {
					if (!word.equals("")) {
						Integer count = 0;
						if (words.containsKey(word))
							count = words.get(word);
						count++;
						wordsCount++;
						words.put(word, count);
					}
				}
				line = br2.readLine();
			}

			// sort frequency map by frequency
			Map<String, Integer> sorted = sortByValues(words);

			// print first 30 words (except stop words)
			int count = 0;
			for (String key : sorted.keySet()) {
				if (count < 30) {
					if (!stopwords.contains(key)) {
						System.out.println(key
								+ ": "
								+ sorted.get(key)
								+ " "
								+ round((double) sorted.get(key)
										/ (double) wordsCount * 100, 2) + "%");
						count++;
					}
				}
			}

			// write words to file
			BufferedWriter out = new BufferedWriter(new FileWriter("data.txt"));

			int c = 0;
			for (String key : sorted.keySet()) {
				out.write(Math.log(c) + " " + Math.log(sorted.get(key)) + "\n");
				c++;
			}

			out.flush();
			out.close();
			
			HashMap<Integer, Integer> wordCount = new HashMap<Integer, Integer>();
			for (String key : sorted.keySet()) {
				int times = 0;
				if (wordCount.containsKey(sorted.get(key)))
					wordCount.put(sorted.get(key),
							wordCount.get(sorted.get(key)) + 1);
				else
					wordCount.put(sorted.get(key), 1);
			}

			// write words to file
			BufferedWriter out2 = new BufferedWriter(new FileWriter("data2.txt"));
			for(Integer i : wordCount.keySet()) {
				out2.write(i + " " + wordCount.get(i) + "\n");
				System.out.println(i + " " + wordCount.get(i) + "\n");
			}
			
			out2.flush();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(
			Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}