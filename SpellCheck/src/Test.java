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

      
    	
        BKTree bktree = new BKTree();
        bktree.ConstructBKTree("cleaned_counts_big.txt");
        List<HashMap> data = loadData();

        Scanner terminalInput = new Scanner(System.in);
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
            break;
        }*/
        String typedPhrase=terminalInput.nextLine();
        ArrayList<Integer> typoPositions=new ArrayList<Integer>();
        //typoPositions.add(0);
        String[] phraseWordArray=typedPhrase.split(" ");
        for(int i=0;i<phraseWordArray.length;i++)
        {
        	if(!bktree.wordDictionary.containsKey(phraseWordArray[i]))
        	{
        		typoPositions.add(i);
        	}
        }
        ConfusionSetLoader confusionsetloader=new ConfusionSetLoader();
        confusionsetloader.loadFiles("confusion_sets.csv");
        confusionsetloader.populateIndex();
        System.out.println(confusionsetloader.confusionReverseIndex.get("piece").candidates);
        confusionsetloader.addNGramCounts("w2_.txt");
        confusionsetloader.addNGramCounts("w3_.txt");
        confusionsetloader.addNGramCounts("w4_.txt");
        confusionsetloader.addNGramCounts("w5_.txt");
        for(int typoPosition:typoPositions)
        {
        	String[] phraseWords=typedPhrase.split(" ");
        	List<String> candidates = new ArrayList<String>(bktree.Search(phraseWords[typoPosition], 3));
            Ranker ranker = new Ranker(data);
            List<List<Object>> scores = ranker.getScores(candidates,phraseWords[typoPosition]);
            System.out.println(scores);
            int scoreCandidateCounter=0;
            for(List<Object> scoreCandidate:scores)
            {
            	phraseWords[typoPosition]=(String)scoreCandidate.get(0);
            	double scoreCandidateWeight=confusionsetloader.generateWeight(phraseWords, typoPosition);
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

        System.out.println(confusionsetloader.nGramCounts.get("a beam"));
        String query1 = "chocolate cake";
        String query2 = "chocolate fake";
        String[] words1 = query1.split(" ");
        String[] words2 = query2.split(" ");
        //double weight1 = confusionsetloader.generateWeight(words1,1);
        //double weight2 = confusionsetloader.generateWeight(words2,1);
        //System.out.println(weight1);
        //System.out.println(weight2);	
        String[] words3 = "roof of the house".split(" ");
        String[] words4 = "peace of mind".split(" ");
        confusionsetloader.generateCandidatePhrases("peace of mind");
        confusionsetloader.generateConfusionIndices("peace of mind");
        System.out.println(confusionsetloader.generateWeight(words3,0));
        ArrayList<String> nGramList=confusionsetloader.generateNGrams(words4,0);
        System.out.println(nGramList);
        System.out.println(confusionsetloader.weighNGrams(nGramList));
        confusionsetloader.spellCheckPhrase("arid dessert");
        
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
