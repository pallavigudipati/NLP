import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.util.StringUtils;
// TODO: change counts for w_c (counts with pos)
public class SpellChecker {
    private int candidatesPerNonWord = 20;
    private int contextWindow = 3;
    private int maxNgrams = 5;

    private BKTree bkTree;
    POSTagger posTagger;
    List<HashMap> ngramsData;

    // Models
    NgramModel ngramModel;
    TagAndNgramModel tagAndNgramModel;
    ContextWordModel contextWordModel;

    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker();
        System.out.println("Initializing SpellChecker");
        spellChecker.initialize();
        @SuppressWarnings("resource")
        Scanner terminalInput = new Scanner(System.in);
        while (true) {
            String typedPhraseRaw = terminalInput.nextLine();
            System.out.println("Level? 0 - Word, 1 - Phrase, 2 - Sentence");
            int level = terminalInput.nextInt();
            spellChecker.check(typedPhraseRaw, level);
        }
    }

    public void initialize() {
        bkTree = new BKTree();
        try {
            bkTree.ConstructBKTree("cleaned_counts_big.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ngramsData = Utils.loadCountsData();
        ngramModel = new NgramModel();
        tagAndNgramModel = new TagAndNgramModel();
        contextWordModel = new ContextWordModel(contextWindow);
        posTagger = new POSTagger();
    }

    public void check(String input, int level) {
        List<List<Object>> scores = null;
        input = input.toLowerCase();
        if (level == 0) {
            // Word level. Assuming the given word is wrong.
            scores = getWordCorrections(input);
        } else if (level == 1) {
            // Phrase : Just using Ngram model.
            // TODO: Add ContextWords Model
            scores = getNgramCorrections(input);
        }
        System.out.println(scores);
    }

    public List<List<Object>> getWordCorrections(String typoWord) {
        List<String> candidates = new ArrayList<String>(bkTree.Search(typoWord,
                3));
        Ranker ranker = new Ranker(ngramsData);
        List<List<Object>> scores = ranker.getScores(candidates, typoWord);
        return scores;
    }

    // Returns all the candidates (multiple substitutions + confusion words) + their edit weights.
    public List<List<Object>> getCandidates(String[] words, List<Integer> typoPositions,
            String rawInput) {
        List<List<List<Object>>> candidatesSequence = new ArrayList<List<List<Object>>>();
        for (int typoPosition : typoPositions) {
            List<List<Object>> scores = getWordCorrections(words[typoPosition]);
            candidatesSequence.add(scores.subList(0,
                    Math.min(candidatesPerNonWord, scores.size())));
        }
        // Gives Edit distance weights. (Probability)
        List<List<Object>> phraseSet = NgramModel.generateSubstitutedPhrases(
                rawInput, candidatesSequence, typoPositions);
        // Using the confusion set to get more candidates. NO PENALTY FOR THIS STEP
        List<List<Object>> phraseAndConfusionSet = new ArrayList<List<Object>>();
        for (List<Object> phrase : phraseSet) {
            phraseAndConfusionSet.add(phrase);
            List<String> confusionPhrases = ngramModel
                    .generateCandidatePhrases((String) phrase.get(0));
            Double editScore = (Double) phrase.get(1);
            for (String confusionPhrase : confusionPhrases) {
                List<Object> confusionObject = new ArrayList<Object>();
                confusionObject.add(confusionPhrase);
                confusionObject.add(editScore);
                phraseAndConfusionSet.add(confusionObject);
            }
        }
        return phraseAndConfusionSet;
    }

    public List<List<Object>> getNgramCorrections(String phrase) {
        String[] phraseWordsArray = phrase.split(" ");
        List<Integer> typoPositions = bkTree.getTypoPositions(phraseWordsArray);
        List<List<Object>> phraseSet = getCandidates(phraseWordsArray, typoPositions, phrase);

        for (List<Object> phraseCandidate : phraseSet) {
            Double contextWeight = 0.0;
            // Context weights for all the substituted words.
            String[] phraseWords = phraseCandidate.get(0).toString().split(" ");
            contextWeight += ngramModel.generateWeight(phraseWords, maxNgrams);
            // Context weight of ContextWordsModel
            contextWeight += contextWordModel.generateWeight(phraseWords);
            // Edit weight
            contextWeight += Math.log((Double) phraseCandidate.get(1));
            phraseCandidate.set(1, contextWeight);
        }

        Collections.sort(phraseSet, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                return -Double.compare((Double) o1.get(1),
                        (Double) o2.get(1));
            }
        });
        return phraseSet;
    }

    public List<List<Object>> getSentenceCorrections(String sentence) {
        String[] wordsArray = sentence.split(" ");
        List<Integer> typoPositions = bkTree.getTypoPositions(wordsArray);
        List<List<Object>> candidateSet = getCandidates(wordsArray, typoPositions, sentence);

        for (List<Object> candidate : candidateSet) {
            Double contextWeight = 0.0;
            String[] candidateWords = ((String) candidate.get(0)).split(" ");
            // Context weight for NgramModel
            contextWeight += ngramModel.generateWeight(candidateWords, maxNgrams);
            // Context weight for TagAndNgramModel
            String[] taggedWordArray = posTagger.tagSentence(StringUtils.join(candidateWords, " "));
            contextWeight += tagAndNgramModel.generateWeight(taggedWordArray, maxNgrams);
            // TODO add more models
            // Context weight of ContextWordsModel
            contextWeight += contextWordModel.generateWeight(candidateWords);
            // Edit weight
            contextWeight += Math.log((Double) candidate.get(1));
            candidate.set(1, contextWeight);
        }

        Collections.sort(candidateSet, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                return -Double.compare((Double) o1.get(1),
                        (Double) o2.get(1));
            }
        });
        return candidateSet;
    }
}
