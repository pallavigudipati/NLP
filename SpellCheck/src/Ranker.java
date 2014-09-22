import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// TODO: generate prior file.
public class Ranker {
    private HashMap<String, Integer> priors;
    private HashMap<String, Integer> unigramCounts;
    private HashMap<String, Integer> bigramCounts;
    private HashMap<String, Integer> wordCounts;
    private static int totalWords = 1043339; // for cleaned-counts-big

    public Ranker(List<HashMap> data) {
        wordCounts = data.get(0);
        priors = data.get(1);
        unigramCounts = data.get(2);
        bigramCounts = data.get(3);
    }

    public List<List<Object>> getScores(List<String> candidates, String incorrectWord) {
        List<List<Object>> scores = new ArrayList<List<Object>>();
        for (String candidate : candidates) {
            // System.out.println(candidate);
            RestrictedEdit restrictedEdit = new RestrictedEdit();
            int distance = restrictedEdit.getDistance(candidate, incorrectWord);
            // System.out.println(distance);
            double likelihood = restrictedEdit.getLikelihood(priors,
                    unigramCounts, bigramCounts);
            // TODO: Better method for smoothing?.
            double frequency = (wordCounts.get(candidate) + 0.5) / totalWords;
            List<Object> candidateAndScore = new ArrayList<Object>();
            candidateAndScore.add(candidate);
            candidateAndScore.add(likelihood * frequency);
            scores.add(candidateAndScore);
        }
        Collections.sort(scores, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                return -Double.compare((Double) o1.get(1), (Double) o2.get(1));
            }
        });
        return scores;
    }
}
