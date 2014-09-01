import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EditPaths {
	private HashMap<String, List<Path>> allPaths;
	
	public EditPaths() {
		allPaths = new HashMap<String, List<Path>>();
	}

	public void updatePaths(int oldX, int oldY, int newX, int newY) {
		if (newX == 0 || newY == 0) {
			updateBorderPath(newX, newY);
			return;
		}
		List<Path> multiplePaths = allPaths.get(coordsToString(newX, newY));
		if (multiplePaths == null) {
			multiplePaths = new ArrayList<Path>();
			allPaths.put(coordsToString(newX, newY), multiplePaths);
		}
		 
		List<Path> oldCellPaths = allPaths.get(coordsToString(oldX, oldY));
		for (Path oldPath : oldCellPaths) {
			Path newPath = new Path();
			newPath.addSubpath(oldPath.getFullPath());
			newPath.addPoint(newX, newY);
			multiplePaths.add(newPath);
		}
	}

	public void updateBorderPath(int x, int y) {
		List<Path> multiplePaths = allPaths.get(coordsToString(x, y));
		if (multiplePaths == null) {
			multiplePaths = new ArrayList<Path>();
			allPaths.put(coordsToString(x, y), multiplePaths);
		}

		Path path = new Path();
		if (x == 0) {
			for (int j = 0; j <= y; ++j) {
				path.addPoint(x, j);
				}
		} else if (y == 0) {
			for (int i = 0; i <= x; ++i) {
				path.addPoint(i, y);
			}
		}
		multiplePaths.add(path);
	}

	public String getPrintableForm(int x, int y) {
		List<Path> multiplePaths = allPaths.get(coordsToString(x, y));
		String printable = "";
		for (Path path : multiplePaths) {
			printable += path.getPrintableForm() + "\n";
		}
		return printable;
	}

	public String extractAllEdits(int x, int y, String source, String target) {
		String toReturn = "";
		List<Path> multiplePaths = allPaths.get(coordsToString(x, y));
		for (Path path : multiplePaths) {
			toReturn += path.extractAllEdits(source, target) + "\n";
		}
		return toReturn;
	}

	public static String pointToString(Point point) {
		return point.x + ";" + point.y;
	}

	public static String coordsToString(int x, int y) {
		return x + ";" + y;
	}

	public static Point stringToPoint(String string) {
		String[] coords = string.split(";");
		return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
	}
}
