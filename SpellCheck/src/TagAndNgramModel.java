
public class TagAndNgramModel extends NgramModel{

    public TagAndNgramModel() {
        this.loadFiles("confusion_sets.csv");
        this.populateIndex();
        Utils.addNGramCounts("ngram-pos/w2c_mapped.txt", nGramCounts);
        Utils.addNGramCounts("ngram-pos/w3c_mapped.txt", nGramCounts);
        Utils.addNGramCounts("ngram-pos/w4c_mapped.txt", nGramCounts);
        Utils.addNGramCounts("ngram-pos/w5c_mapped.txt", nGramCounts);
    }
}
