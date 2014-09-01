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

	public void extractAllEdits(String source, String target) {
		
	}
}
