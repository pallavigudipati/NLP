import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static void main(String[] args)throws FileNotFoundException,IOException {
		BKTree bktree=new BKTree();
		String typo= "jello";
		bktree.ConstructBKTree("cleaned_counts_big.txt");
		List<String> candidates = new ArrayList<String>(bktree.Search(typo, 2));
		Ranker ranker = new Ranker();
		ranker.loadData();
		List<Double> scores = ranker.getScores(candidates, typo);
		for (int i = 0; i < candidates.size(); ++i) {
			System.out.println(candidates.get(i) + "\t" + scores.get(i));
		}
	}
}
