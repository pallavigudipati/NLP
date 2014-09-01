import java.util.Arrays;


public class RestrictedEdit {
	public String source;
	public String target;
	int[][] characterMatrix;
	EditPaths editPaths;
	
	public int getDistance(String source, String target) {
		this.source = source;
		this.target = target;
		editPaths = new EditPaths();
		this.characterMatrix = new int[source.length() + 1][target.length() + 1];
		for (int[] row : characterMatrix) {
			Arrays.fill(row, -1);
		}
		return editDistance(source.length(), target.length());
	}

	public int editDistance(int i, int j) {
		if (characterMatrix[i][j] != -1) {
			return characterMatrix[i][j];
		}
		if (j == 0) {
			setCharacterMatrix(i, j, i);
			editPaths.updatePaths(0, 0, i, j);
			return i;
		}
		if (i == 0) {
			setCharacterMatrix(i, j, j);
			editPaths.updatePaths(0, 0, i, j);
			return j;
		}
		int cost = source.charAt(i - 1) == target.charAt(j - 1) ? 0 : 1;
		int minEdit = Math.min(Math.min(editDistance(i - 1, j) + 1,
				editDistance(i, j - 1) + 1), editDistance(i - 1, j - 1) + cost);
		int transposeCost = -1;
		if (i >= 2 && j >= 2 && source.charAt(i - 2) == target.charAt(j - 1)
				&& source.charAt(i - 1) == target.charAt(j - 2)) {
			transposeCost = editDistance(i - 2, j - 2) + cost;
			minEdit = Math.min(minEdit, transposeCost);
			if (minEdit == transposeCost) {
				editPaths.updatePaths(i - 2, j - 2, i, j);
			}
		}
		setCharacterMatrix(i, j, minEdit);
		updateForNormalTransitions(i, j, minEdit, cost);
		return minEdit;
	}

	public void updateForNormalTransitions(int i, int j, int minEdit, int cost) {
		if (minEdit == characterMatrix[i - 1][j] + 1) {
			editPaths.updatePaths(i - 1, j, i, j);
		}
		if (minEdit == characterMatrix[i][j - 1] + 1) {
			editPaths.updatePaths(i, j - 1, i, j);
		}
		if (minEdit == characterMatrix[i - 1][j - 1] + cost) {
			editPaths.updatePaths(i - 1, j - 1, i, j);
		}
	}

	public String getPrintableForm() {
		return editPaths.getPrintableForm(source.length(), target.length());
	}

	public void setCharacterMatrix(int i, int j, int value) {
		characterMatrix[i][j] = value;
	}
}
