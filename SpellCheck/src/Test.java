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
        NgramModel confusionsetloader = new NgramModel();
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