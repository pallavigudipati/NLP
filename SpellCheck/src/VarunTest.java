import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*Sorry for this haphazard indentation
  Examples which worked:
  dessrt sun
*/
public class VarunTest {

    public static void main(String[] args) {
        BKTree bktree = new BKTree();
        try {
            bktree.ConstructBKTree("cleaned_counts_big.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        NgramModel confusionsetloader=new NgramModel();
		/*Goes through a phrase, generates all substituted phrases, ranks them with context+wordspellcheck logsum*/
        List<List<List<Object>>> candidatesSequence = new ArrayList<List<List<Object>>>();
        for(int typoPosition:typoPositions)
        {
        	String[] phraseWords=typedPhrase.split(" ");
        	List<String> candidates = new ArrayList<String>(bktree.Search(phraseWords[typoPosition], 3));
            Ranker ranker = new Ranker(data);
            List<List<Object>> scores = ranker.getScores(candidates,phraseWords[typoPosition]);
            List<List<Object>> trimmedScores = new ArrayList<List<Object>>();
            for(int i=0;i<scores.size();i++)
            {
            	if(i>20)
            	{
            		break;
            	}
            	else
            	{
            		trimmedScores.add(scores.get(i));
            	}
            }
            candidatesSequence.add(trimmedScores);
        }
        List<List<Object>> phraseSet=generateSubstitutedPhrases(typedPhrase,candidatesSequence,typoPositions);
        List<List<Object>> updatedPhraseSet= new ArrayList<List<Object>>(); 
        System.out.println(phraseSet);
        for(List<Object> phraseCandidate:phraseSet)
        {
        	double contextWeight=0.0;
        	for(Integer typoPosition:typoPositions)
        	{
        		String[] phraseWords=phraseCandidate.get(0).toString().split(" ");
        		contextWeight+=confusionsetloader.generateWeight(phraseWords, typoPosition);        		
        	}
        	List<Object> newPhrase=new ArrayList<Object>();
        	newPhrase.add(phraseCandidate.get(0));
        	newPhrase.add(Math.log((Double)phraseCandidate.get(1))+contextWeight);
        	updatedPhraseSet.add(newPhrase);
        }
        System.out.println(updatedPhraseSet);
        Collections.sort(updatedPhraseSet, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                return -Double.compare((Double) o1.get(1), (Double) o2.get(1));
            }
        });
        System.out.println(updatedPhraseSet);
}
    
}
