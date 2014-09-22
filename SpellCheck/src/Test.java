import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.util.StringUtils;

public class Test {

    public static void main(String[] args) {
        BKTree bktree = new BKTree();
        try {
            bktree.ConstructBKTree("cleaned_counts_big.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<HashMap> data = Utils.loadCountsData();
        Scanner terminalInput = new Scanner(System.in);
        POSTagger tagger = new POSTagger();
        ConfusionSetLoader confusionsetloader = new ConfusionSetLoader();
        //  TODO: Make everything into lower case
        while (true) {
            String typedPhraseRaw=terminalInput.nextLine();
            ArrayList<Integer> typoPositions=new ArrayList<Integer>();
            String[] phraseWordArrayRaw = typedPhraseRaw.split(" "); 
            for(int i=0;i<phraseWordArrayRaw.length;i++)
            {
            	if(!bktree.wordDictionary.containsKey(phraseWordArrayRaw[i]))
            	{
            		typoPositions.add(i);
            	}
            }
            for(int typoPosition:typoPositions)
            {
            	List<String> candidates = new ArrayList<String>(bktree.Search(phraseWordArrayRaw[typoPosition], 3));
                Ranker ranker = new Ranker(data);
                List<List<Object>> scores = ranker.getScores(candidates,phraseWordArrayRaw[typoPosition]);
                // System.out.println(scores);
                int scoreCandidateCounter=0;
                for(List<Object> scoreCandidate:scores)
                {
                	phraseWordArrayRaw[typoPosition]=(String)scoreCandidate.get(0);
                	List<String> phaseWordList = tagger.tagSentence(StringUtils.join(phraseWordArrayRaw, " "));
                    String[] phraseWordArray = phaseWordList.toArray(new String[phaseWordList.size()]);
                	double scoreCandidateWeight=confusionsetloader.generateWeight(phraseWordArray, typoPosition);
                	double editWeight=(Double)scoreCandidate.get(1);
                	//scoreCandidateWeight=0.0;
                	double logEditWeight=Math.log(editWeight);
                	double totalWeight=logEditWeight+scoreCandidateWeight;
                	System.out.println(scoreCandidate.get(0)+" "+totalWeight+" "+logEditWeight+" "+scoreCandidateWeight);
                	scoreCandidateCounter+=1;
                	if(scoreCandidateCounter>10)
                	{
                		break;
                	}
                }
            }
        }
    }

    /*
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
    }*/
}
/*
while(true) {
    String typo = terminalInput.nextLine();
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
}
    break;
}*/