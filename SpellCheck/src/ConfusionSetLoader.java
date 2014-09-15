import java.io.*;
import java.util.*;
public class ConfusionSetLoader 
{
	public static ArrayList<ConfusionSet> confusionSetList = new ArrayList<ConfusionSet>();
	public static HashMap<String,ConfusionSet> confusionReverseIndex = new HashMap<String,ConfusionSet>();
	public static HashMap<String,Double> nGramCounts = new HashMap<String,Double>();
    public static void loadFiles(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                //Create a new confusion set
                ConfusionSet confusionSet = new ConfusionSet();
                for(String part:parts)
                {
                	confusionSet.candidates.add(part);
                }
                confusionSetList.add(confusionSet);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files");
        }
    }
    public static void populateIndex()
    {
    	for(ConfusionSet confusionSet:confusionSetList)
    	{
    		for(String confusionWord:confusionSet.candidates)
    		{
    			confusionReverseIndex.put(confusionWord,confusionSet);
    		}
    	}
    }
    public static void addNGramCounts(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                double nGramCount=Double.parseDouble(parts[0]);
                String nGram="";
                int counter=0;
                for(String part:parts)
                {
                	if(counter==0)
                	{
                		counter+=1;
                		continue;
                	}
                	if(counter==1)
                	{
                		nGram=part;
                	}
                	else
                	{
                		nGram=nGram+" "+part;
                	}
                	counter+=1;
                }
                nGramCounts.put(nGram,nGramCount);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files");
        }
    }
    /*
    public static ArrayList<String> generateCandidatePhrases(String phrase)
    {
    	ArrayList<String> phraseSet=new ArrayList<String>();
    	phraseSet.add(phrase);
    	String[] words=line.split(" ");
    	for(String word:words)
    	{
    		
    	}
    }
    */
    public double getCount(String ngram)
    {
    	Double ngramCount = nGramCounts.get(ngram); 
    	if(ngramCount == null) {
    		return 0;
    	}
    	return ngramCount;
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
            weight += Math.log(1 + getCount(rightString));
        }
        // List<String> mergedGrams = new ArrayList<String>();
        for (int i = 0; i < leftGrams.size(); ++i) {
            for (int j = 0; j < rightGrams.size(); ++j) {
                if (i + j + 3 > 5) {
                    break;
                }
                // mergedGrams.add(leftGrams.get(i) + " " + rightGrams.get(j));
                // TODO: 
                weight += Math.log(1 + getCount(leftGrams.get(i) + " " + rightGrams.get(j)));
            }
            // mergedGrams.add(leftGrams.get(i) + " " + confusionWord);
           System.out.println(leftGrams.get(i) + " " + confusionWord); //Write a wrapper
            weight += Math.log(1 + getCount(leftGrams.get(i) + " " + confusionWord));
        }
        // mergedGrams.addAll(rightGrams);
        return weight;
    }   
}
