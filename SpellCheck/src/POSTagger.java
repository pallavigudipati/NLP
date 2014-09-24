import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger {
    public HashMap<String, String> pennToInternal;
    public MaxentTagger tagger;

    public POSTagger() {
        tagger = new MaxentTagger("pos-model/english-bidirectional-distsim.tagger");
        loadMappings();
    }

    public static void main(String[] args) {
        POSTagger posTagger = new POSTagger();
        MaxentTagger tagger = new MaxentTagger("pos-model/english-bidirectional-distsim.tagger");
        posTagger.loadMappings();
        String example = "It is Max's girl";
        long startTime = System.currentTimeMillis();
        String[] taggedString = posTagger.tagSentence(example);
        long endTime = System.currentTimeMillis();
        System.out.println(taggedString);
        System.out.println(endTime - startTime);
    }

    public void loadMappings() {
        String filename = "pos-model/penn_to_internal.txt";
        pennToInternal = new HashMap<String, String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                pennToInternal.put(parts[0], parts[1]);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files");
        }
    }

    public String[] tagSentence(String sentence) {
        String pennSentence = tagger.tagString(sentence);
        List<String> internalSentence = new ArrayList<String>();
        String[] words = pennSentence.split(" ");
        for (String word : words) {
            String[] tag = word.split("_");
            internalSentence.add(tag[0] + "_" + pennToInternal.get(tag[1]));
        }
        return internalSentence.toArray(new String[internalSentence.size()]);
    }
}
