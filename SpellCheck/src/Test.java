import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<String> candidates = new ArrayList<String>(Arrays.asList("hello", "jello", "mellow"));
		Ranker ranker = new Ranker();
		ranker.loadData();
		List<Double> scores = ranker.getScores(candidates, "yello");
		for (int i = 0; i < candidates.size(); ++i) {
			System.out.println(candidates.get(i) + "\t" + scores.get(i));
		}
	}
}
