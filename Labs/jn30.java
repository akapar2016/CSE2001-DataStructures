package Labs;

public class jn30 {
	public static int binarySearchIterative(int[ ] data, int target) {
		int low = 0;
		int high = data.length - 1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (target == data[mid]) // found a match
				return mid;
			else if (target < data[mid])
				high = mid - 1; // only consider values left of mid
			else
				low = mid + 1; // only consider values right of mid
		}
		return -1; // loop ended without success
	}
	
	
	
	
	
	
	
	public static int trial (String x, int n) {
		if(n == x.length()) {
			return 0;
		}
		int sub =  Integer.parseInt(x.substring(x.length() - (n+1), x.length() - (n)));
		System.out.println(sub);
		return (int) (sub * (Math.pow(10, n)) + trial(x, n+1));
	}
	
	
	
	public static double harmonic (double n) {
		if(n == 1) {
			return 1;
		}
		return 1/n + harmonic(n-1);
		
	}
	
	
	public static void main (String[] args) {
		System.out.print(harmonic(77));
	}
}
