import java.util.HashMap;


public class ContextWordModel {

    private int WINDOW = 3; // Default
    private HashMap<String, HashMap<String, Double>> contextWordProbs;
    private HashMap<String, Integer> wordCounts;

    public ContextWordModel(int window) {
        this.WINDOW = window;
        contextWordProbs = new HashMap<String, HashMap<String, Double>>();
        wordCounts = new HashMap<String, Integer>();
        Utils.loadContextWords("brown-corpus/brown_context_counts.txt", contextWordProbs);
        System.out.println("read file : context");
        Utils.loadBrownCounts("brown-corpus/brown_occurence_counts.txt", wordCounts);
        System.out.println("read file : occurences");
    }

    public static void main(String[] args) {
        String query = "deserts have hot sand"; 
        String[] queryWords = query.split(" ");
        ContextWordModel test = new ContextWordModel(3);
        double prob = test.generateWeight(queryWords);
        System.out.println(prob);
    }

    public double generateWeight(String[] words) {
        Double globalProb = 0.0;
        Double globalDenom = 0.0;
        for (int typoPosition = 0; typoPosition < words.length; ++typoPosition) {
            HashMap<String, Double> contextWords = contextWordProbs.get(words[typoPosition]);
            if (contextWords == null) {
                System.out.println("Context word does not exist");
                continue;
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
            globalProb += prob;
            globalDenom += denominator;
        }
        return globalProb + globalDenom;
    }
    /*
    public double generateWeight(String[] words, int typoPosition) {
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
    }*/
}
