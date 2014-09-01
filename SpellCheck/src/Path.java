import java.awt.Point;
import java.util.ArrayList;
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

	public String extractAllEdits(String source, String target) {
		String xSource = "~" + source;
		String xTarget = "~" + target;
		String toReturn = "";
		Point prevPoint = pathCoords.get(0);
		for (int i = 1; i < pathCoords.size(); ++i) {
			Point currPoint = pathCoords.get(i);
			String correct = "";
			String wrong = "";
			if (prevPoint.x == currPoint.x) {
				wrong += "" + xTarget.charAt(prevPoint.y) + xTarget.charAt(currPoint.y);
				correct += "" + xTarget.charAt(prevPoint.y);
			} else if (prevPoint.y == currPoint.y) {
				wrong += "" + xSource.charAt(prevPoint.x);
				correct += "" + xSource.charAt(prevPoint.x) 
						+ xSource.charAt(currPoint.x); 
			} else if (prevPoint.x + 1 == currPoint.x && prevPoint.y + 1 == currPoint.y) {
				wrong += "" + xTarget.charAt(currPoint.y);
				correct += "" + xSource.charAt(currPoint.x);
			} else if (prevPoint.x + 2 == currPoint.x && prevPoint.y + 2 == currPoint.y) {
				wrong += "" + xTarget.charAt(prevPoint.y + 1) + xTarget.charAt(currPoint.y);
				correct += "" + xSource.charAt(prevPoint.x + 1) + xSource.charAt(currPoint.x);
			}
			toReturn += wrong + "|" + correct + " ; ";
			prevPoint = currPoint;
		}
		return toReturn;
	}
}
