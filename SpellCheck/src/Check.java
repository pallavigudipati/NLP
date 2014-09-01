
public class Check {
	public static void main(String[] args) {
		RestrictedEdit res = new RestrictedEdit();
		int distance = res.getDistance("ca", "ac");
		String print = res.getPrintableForm();
		System.out.println(print);
		System.out.println(distance);
	}
}
