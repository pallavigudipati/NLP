import java.util.HashMap;


public class ContextWords {

    private int WINDOW = 3; // Default
    private HashMap<String, HashMap<String, Double>> contextWordProbs;
    private HashMap<String, Integer> wordCounts;

    public ContextWords(int window) {
        this.WINDOW = window;
        Utils.loadContextWords("brown-corpus/brown_context_counts.txt", contextWordProbs);
        Utils.loadBrownCounts("brown-corpus/brown_occurence_counts.txt", wordCounts);
    }

    public static void main(String[] args) {
        String query = "to be or not to be"; 
        String[] queryWords = query.split(" ");
        ContextWords test = new ContextWords(3);
        test.getProbability(queryWords, 2);
    }

    public double getProbability(String[] words, int typoPosition) {
        HashMap<String, Double> contextWords = contextWordProbs
                .get(words[typoPosition]);
        if (contextWords == null) {
            System.out.println("Context word does not exist");
            return 0.0;
        }
        Double prob = 0.0;
        Double denominator = 0.0;
        // Left of the typo
        for (int i = typoPosition - 1; i >= 0 && i >= typoPosition - WINDOW; --i) {
            // TODO: Doing + 1 even here
            double contextCount = contextWords.get(words[i]) == null ? 0.0
                    : contextWords.get(words[i]);
            prob += Math.log(1 + contextCount);
            int wordCount = wordCounts.get(words[i]) == null ? 0 : wordCounts
                    .get(words[i]);
            denominator += -Math.log(1 + wordCount);
        }
        for (int i = typoPosition + 1; i <= typoPosition + WINDOW
                && i < words.length; ++i) {
            double contextCount = contextWords.get(words[i]) == null ? 0.0
                    : contextWords.get(words[i]);
            prob += Math.log(1 + contextCount);
            int wordCount = wordCounts.get(words[i]) == null ? 0 : wordCounts
                    .get(words[i]);
            denominator += -Math.log(1 + wordCount);
        }
        return prob + denominator;
    }
}
