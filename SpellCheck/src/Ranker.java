import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: generate prior file.
public class Ranker {
	private static HashMap<String, Integer> priors;
	private static HashMap<String, Integer> unigramCounts;
	private static HashMap<String, Integer> bigramCounts;
	private static HashMap<String, Integer> wordCounts;
	private static int totalWords = 40000000; // TODO change

	public List<Double> getScores(List<String> candidates, String incorrectWord) {
		List<Double> scores = new ArrayList<Double>();
		for (String candidate : candidates) {
			RestrictedEdit restrictedEdit = new RestrictedEdit();
			double likelihood = restrictedEdit.getLikelihood(priors, unigramCounts, bigramCounts);
			// TODO: We need to divide it by total number of words (not that imp) and SMOOTH it.
			// For now just putting 0.5 for non-existent values to avoid runtime errors.
			double wordCount = wordCounts.get(candidate) == null ? 0.5 : wordCounts.get(candidate);
			scores.add(likelihood * wordCount);
		}
		return scores;
	}

	public void loadData() {
		loadFiles("cleaned_counts.txt", wordCounts);
		loadFiles("priors.txt", priors);
		loadFiles("unigram_counts.txt", unigramCounts);
		loadFiles("bigram_counts.txt", bigramCounts);
	} 

	private void loadFiles(String filename, HashMap<String, Integer> table) {
		table = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				table.put(parts[0], Integer.parseInt(parts[1]));
			}
		} catch (Exception e) {
			System.out.println("Not able to read files");
		}
	}

	/*
	public void loadUnigrams(String filename) {
		unigramCounts = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				unigramCounts.put(parts[0], Integer.parseInt(parts[1]));
			}
		} catch (Exception e) {
			System.out.println("Not able to read unigrams");
		}
	}

	public void loadBigrams(String filename) {
		bigramCounts = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				bigramCounts.put(parts[0], Integer.parseInt(parts[1]));
			}
		} catch (Exception e) {
			System.out.println("Not able to read bigrams");
		}
	}

	public void parse(String filename) {
		priors = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("count_1edit.txt"));
			String line = null;
			while ((line = reader.readLine()) != null) {
			    String[] parts = line.split("|");
			    if (parts.length >= 2) {
			    	String wrong = parts[0];
			    }
			}
		} catch (Exception e) {
			System.out.println("Not able to get ngram priors.");
		}
	}
	*/
}
