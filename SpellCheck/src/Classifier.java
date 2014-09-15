import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Classifier {
    private HashMap<String, Integer> ngramCounts;

    public Classifier() {
        
    }

    public double generateWeight(String[] phrase, int confusionIndex) {
        double weight = 0;
        int sizeOfPhrase = phrase.length;
        String confusionWord = phrase[confusionIndex];
        List<String> ngrams = new ArrayList<String>();
        List<String> leftGrams = new ArrayList<String>(); // small to big
        String leftString = "";
        for (int i = confusionIndex - 1; i >= 0 && i >= confusionIndex - 4; --i) {
            leftString = phrase[i] + " " + leftString;
            leftGrams.add(leftString);
        }
        List<String> rightGrams = new ArrayList<String>();
        String rightString = phrase[confusionIndex];
        // String rightString = "";
        for (int i = confusionIndex + 1; i < sizeOfPhrase && i <= confusionIndex + 4; ++i) {
            rightString = rightString + " " + phrase[i];
            rightGrams.add(rightString);
            weight += Math.log(1 + ngramCounts.get(rightString));
        }
        // List<String> mergedGrams = new ArrayList<String>();
        for (int i = 0; i < leftGrams.size(); ++i) {
            for (int j = 0; j < rightGrams.size(); ++j) {
                if (i + j + 3 > 5) {
                    break;
                }
                // mergedGrams.add(leftGrams.get(i) + " " + rightGrams.get(j));
                // TODO: 
                weight += Math.log(1 + ngramCounts.get(leftGrams.get(i) + " " + rightGrams.get(j)));
            }
            // mergedGrams.add(leftGrams.get(i) + " " + confusionWord);
            weight += Math.log(1 + ngramCounts.get(leftGrams.get(i) + " " + confusionWord));
        }
        // mergedGrams.addAll(rightGrams);
        return weight;
    }
}
