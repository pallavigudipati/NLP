
public class Check {
	public static void main(String[] args) {
		RestrictedEdit res = new RestrictedEdit();
		int distance = res.getDistance("ca", "abc");
		String print2 = res.getPrintableForm();
		String print = res.extractAllEdits();
		System.out.println(print);
		System.out.println(print2);
		System.out.println(distance);
	}
}
