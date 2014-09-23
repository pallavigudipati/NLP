import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Utils {

    public static List<HashMap> loadCountsData() {
        HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
        HashMap<String, Integer> priors = new HashMap<String, Integer>();
        HashMap<String, Integer> unigramCounts = new HashMap<String, Integer>();
        HashMap<String, Integer> bigramCounts = new HashMap<String, Integer>();
        loadFiles("cleaned_counts_big.txt", wordCounts);
        loadFiles("cleaned_count_1edit.txt", priors);
        loadFiles("unigram_counts.txt", unigramCounts);
        loadFiles("bigram_counts.txt", bigramCounts);
        List<HashMap> data = new ArrayList<HashMap>();
        data.add(wordCounts);
        data.add(priors);
        data.add(unigramCounts);
        data.add(bigramCounts);
        return data;
    }

    private static void loadFiles(String filename, HashMap<String, Integer> table) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                table.put(parts[0], Integer.parseInt(parts[1]));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files");
        }
    }

    public static void addNGramCounts(String filename, HashMap<String, Double> table) {
        int nGramsLoadedCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                double nGramCount = Double.parseDouble(parts[0]);
                nGramsLoadedCount += nGramCount;
                String nGram = "";
                int counter = 0;
                for (String part : parts) {
                    if (counter == 0) {
                        counter += 1;
                        continue;
                    }
                    if (counter == 1) {
                        nGram = part;
                    } else {
                        nGram = nGram + " " + part;
                    }
                    counter += 1;
                }
                table.put(nGram,nGramCount);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files");
        }
        System.out.println(nGramsLoadedCount);
    }

    // Eg. do_score 1
    public static void loadContextWords(String filename,
            HashMap<String, HashMap<String, Double>> contextWords) {
        if (contextWords == null) {
            contextWords = new HashMap<String, HashMap<String, Double>>();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] count = line.split(" ");
                String[] words = count[0].split("_");
                if (contextWords.get(words[0]) == null) {
                    contextWords.put(words[0], new HashMap<String, Double> ());
                }
                contextWords.get(words[0]).put(words[1], Double.parseDouble(count[1]));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files: " + filename);
        }
    }

    // Eg. fawn 1
    public static void loadBrownCounts(String filename, HashMap<String, Integer> counts) {
        if (counts == null) {
            counts = new HashMap<String, Integer>();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                counts.put(parts[0], Integer.parseInt(parts[1]));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files: " + filename);
        }
    }
}
