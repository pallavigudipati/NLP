import java.util.HashMap;


public class ContextWords {

    private int WINDOW = 3; // Default
    private HashMap<String, HashMap<String, Double>> contextWordProbs;

    public ContextWords(int window, HashMap<String, HashMap<String, Double>> contextWordProbs) {
        this.WINDOW = window;
        this.contextWordProbs = contextWordProbs;
    }

    public static void main(String[] args) {
        String query = "0 1 2 3 4 5 6 7"; 
        String[] queryWords = query.split(" ");
        ContextWords test = new ContextWords(3, new HashMap<String, HashMap<String, Double>>());
        test.getProbability(queryWords, 7);
    }

    public double getProbability(String[] words, int typoPosition) {
        HashMap<String, Double> contextWords = contextWordProbs.get(words[typoPosition]);
        Double prob = 0.0;
        // Left of the typo
        for (int i = typoPosition - 1; i >= 0 && i >= typoPosition - WINDOW; --i) {
            // TODO: Doing + 1 even here
            prob += Math.log(1 + contextWords.get(words[i]));
        }
        for (int i = typoPosition + 1; i <= typoPosition + WINDOW && i < words.length; ++i) {
            prob += Math.log(1 + contextWords.get(words[i]));
        }
        return prob;
    }
}
