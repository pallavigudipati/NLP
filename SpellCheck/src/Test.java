import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws FileNotFoundException,
            IOException {
        /*
>>>>>>> 627b2895fb90109b590eec48f41a256954b29fbc
        BKTree bktree = new BKTree();

        // String typo = "aisel";
        /*
        bktree.ConstructBKTree("cleaned_counts_big.txt");
        List<HashMap> data = loadData();

        Scanner terminalInput = new Scanner(System.in);
        while(true) {
            String typo = terminalInput.nextLine();;
            long startTime = System.currentTimeMillis();
            List<String> candidates = new ArrayList<String>(bktree.Search(typo, 3));
            Ranker ranker = new Ranker(data);
            List<List<Object>> scores = ranker.getScores(candidates, typo);
            long endTime = System.currentTimeMillis();
            for (int i = 0; i < candidates.size() && i < 20; ++i) {
                if (Double.compare((Double) scores.get(i).get(1), 0.0) != 0) {
                    System.out.println(scores.get(i).get(0) + "\t" + scores.get(i).get(1));
                }

            }
            System.out.println("Total time taken: " + (endTime - startTime));
        }*/
        ConfusionSetLoader confusionsetloader=new ConfusionSetLoader();
        confusionsetloader.loadFiles("confusion_sets.csv");
        confusionsetloader.populateIndex();
        System.out.println(confusionsetloader.confusionReverseIndex.get("piece").candidates);
        confusionsetloader.addNGramCounts("w2_.txt");
        confusionsetloader.addNGramCounts("w3_.txt");
        confusionsetloader.addNGramCounts("w4_.txt");
        confusionsetloader.addNGramCounts("w5_.txt");
        System.out.println(confusionsetloader.nGramCounts.get("a beam"));
        String query = "piece of mind";
        String[] words = query.split(" ");
        double weight1 = confusionsetloader.generateWeight(words, 0);
        double weight2 = confusionsetloader.generateWeight(new String[]{"peace" ,"of","mind"}, 0);
        System.out.println(weight1);
        System.out.println(weight2);
    }

    public static List<HashMap> loadData() {
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

}
