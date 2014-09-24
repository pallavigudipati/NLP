import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.util.StringUtils;

public class SentenceTest {
    private BKTree bkTree;
    private List<HashMap> countsData;
    private POSTagger posTagger;
    private NgramModel ngramModel;

    public SentenceTest() {
        bkTree = new BKTree();
        try {
            bkTree.ConstructBKTree("cleaned_counts_big.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        countsData = Utils.loadCountsData();
        posTagger = new POSTagger();
        ngramModel = new NgramModel();
    }

    public void correctSentence() {
        @SuppressWarnings("resource")
        Scanner terminalInput = new Scanner(System.in);
        //  TODO: Make everything into lower case
        while (true) {
            String typedPhraseRaw = terminalInput.nextLine();
            ArrayList<Integer> typoPositions = new ArrayList<Integer>();
            String[] phraseWordArrayRaw = typedPhraseRaw.split(" "); 
            for (int i = 0; i < phraseWordArrayRaw.length; i++) {
                if (!bkTree.wordDictionary.containsKey(phraseWordArrayRaw[i])) {
                    typoPositions.add(i);
                }
            }
            for (int typoPosition : typoPositions) {
                List<String> candidates = new ArrayList<String>(bkTree.Search(
                        phraseWordArrayRaw[typoPosition], 3));
                Ranker ranker = new Ranker(countsData);
                List<List<Object>> scores = ranker.getScores(candidates,
                        phraseWordArrayRaw[typoPosition]);
                // System.out.println(scores);
                int scoreCandidateCounter = 0;
                for (List<Object> scoreCandidate : scores) {
                    phraseWordArrayRaw[typoPosition] = (String) scoreCandidate.get(0);
                    String[] phaseWordArray = posTagger.tagSentence(StringUtils
                            .join(phraseWordArrayRaw, " "));
                    double scoreCandidateWeight = ngramModel
                            .generateWeight(phraseWordArray, typoPosition);
                    double editWeight = (Double) scoreCandidate.get(1);
                    // scoreCandidateWeight=0.0;
                    double logEditWeight = Math.log(editWeight);
                    double totalWeight = logEditWeight + scoreCandidateWeight;
                    System.out.println(scoreCandidate.get(0) + " "
                            + totalWeight + " " + logEditWeight + " "
                            + scoreCandidateWeight);
                    scoreCandidateCounter += 1;
                    if (scoreCandidateCounter > 10) {
                        break;
                    }
                }
            }
        }

    }
}
