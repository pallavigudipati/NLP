import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Path {
	private List<Point> pathCoords;

	public void addPoint(int x, int y) {
		Point newPoint = new Point(x, y);
		if (pathCoords == null) {
			pathCoords = new ArrayList<Point>();
		}
		pathCoords.add(newPoint);
	}

	public void addSubpath(List<Point> subpath) {
		if (pathCoords == null) {
			pathCoords = new ArrayList<Point>();
		}
		pathCoords.addAll(subpath);
	}

	public List<Point> getFullPath() {
		return pathCoords;
	}

	public Path clone(Path path) {
		Path copy = new Path();
		copy.addSubpath(path.getFullPath());
		return copy;
	}

	public String getPrintableForm() {
		String printable = "";
		for (Point point : pathCoords) {
			printable += "(" + point.x + ";" + point.y + ")";
		}
		return printable;
	}

	public List<List<String>> extractAllEdits(String source, String target) {
		String xSource = "~" + source;
		String xTarget = "~" + target;
		List<List<String>> allEdits = new ArrayList<List<String>>();
		Point prevPoint = pathCoords.get(0);
		for (int i = 1; i < pathCoords.size(); ++i) {
			Point currPoint = pathCoords.get(i);
			String correct = "";
			String wrong = "";
			String type = "";
			// Insertion
			if (prevPoint.x == currPoint.x) {
				wrong += "" + xTarget.charAt(prevPoint.y) + xTarget.charAt(currPoint.y);
				correct += "" + xTarget.charAt(prevPoint.y);
				type += "I";
			} else if (prevPoint.y == currPoint.y) {
				// Deletion
				wrong += "" + xSource.charAt(prevPoint.x);
				correct += "" + xSource.charAt(prevPoint.x) 
						+ xSource.charAt(currPoint.x);
				type += "D";
			} else if (prevPoint.x + 1 == currPoint.x && prevPoint.y + 1 == currPoint.y) {
				// Substitution
				wrong += "" + xTarget.charAt(currPoint.y);
				correct += "" + xSource.charAt(currPoint.x);
				type += "S";
			} else if (prevPoint.x + 2 == currPoint.x && prevPoint.y + 2 == currPoint.y) {
				// Reversal
				wrong += "" + xTarget.charAt(prevPoint.y + 1) + xTarget.charAt(currPoint.y);
				correct += "" + xSource.charAt(prevPoint.x + 1) + xSource.charAt(currPoint.x);
				type += "R";
			}
			allEdits.add(new ArrayList<String>(Arrays.asList(type, wrong + "|" + correct)));
			prevPoint = currPoint;
		}
		return allEdits;
	}

	// Needs to be multiplied by the prior of the correct word.
	public double getLikelihood(HashMap<String, Integer> priors,
			HashMap<String, Integer> unigramCounts,
			HashMap<String, Integer> bigramCounts, String source, String target) {
		List<List<String>> allEdits = extractAllEdits(source, target);
		double likelihood = 1;
		for (List<String> typeEdit : allEdits) {
			// P(w|c) => xy|x => Insertion : norm = unigramCount (x)
			// 		  => x|xy => Deletion : norm = bigramCount(xy)
			// 		  => x|y => Substitution: norm = unigramCount(y)
			// 		  => xy|yx => Reversal : norm = bigramCount(yx)
			String edit = typeEdit.get(1);
			String[] parts = edit.split("\\|");
			String correct = parts[1];
			String wrong = parts[0];
			if (correct.equals(wrong)) {
				continue;
			}
			double normalization = 0;
			double freq = priors.get(typeEdit.get(1));
			
			if (typeEdit.get(0).equals("I") || typeEdit.get(0).equals("S")) {
				normalization = unigramCounts.get(correct);
			} else if (typeEdit.get(0).equals("D") || typeEdit.get(0).equals("R")) {
				normalization = bigramCounts.get(correct);
			}
			likelihood *= freq / normalization;
		}
		return likelihood;
	}
}