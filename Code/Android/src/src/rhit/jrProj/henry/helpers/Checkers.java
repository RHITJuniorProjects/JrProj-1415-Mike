package rhit.jrProj.henry.helpers;

public class Checkers {
	
	public static Object checkNotNull(Object object) throws Exception {
		if (object == null) {
			throw new Exception("Object was null");
		}
		return object;
	}
	
	public static int checkNotNegative(int i) throws Exception {
		if (i < 0) {
			throw new Exception("int was negative");
		}
		return i;
	}

}
