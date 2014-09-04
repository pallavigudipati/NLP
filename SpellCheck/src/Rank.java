import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;


public class Rank {
	private HashMap<String, Integer> priors;
	
	public static void main(String[] args) {
		RestrictedEdit res = new RestrictedEdit();
		int distance = res.getDistance("ca", "abc");
		String print2 = res.getPrintableForm();
		String print = res.extractAllEdits();
		System.out.println(print);
		System.out.println(print2);
		System.out.println(distance);
	}

	public void parse(String filename) {
		priors = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("count_1edit.txt"));
			String line = null;
			while ((line = reader.readLine()) != null) {
			    String[] parts = line.split("|");
			    if (parts.length >= 2) {
			    	String wrong = parts[0];
			    }
			}
		} catch (Exception e) {
			System.out.println("Not able to get ngram priors.");
		}
	}
}
