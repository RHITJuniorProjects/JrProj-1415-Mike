package rhit.jrProj.henry.firebase;


//TODO

public class Enums {
	public enum Role {
		DEVELOPER, LEAD;

		public static Role parse(String value) {
			if (value.equalsIgnoreCase("lead")) {
				return Role.LEAD;
			}
			return Role.DEVELOPER;
		}
	}

	public enum Category {
		Bug,
	}
	public enum ObjectType{
		PROJECT, MILESTONE, TASK, MEMBER, BOUNTY
	}
	public final static String NEW= "New";
	public final static String IMP="Implementation";
	public final static String TEST="Testing";
	public final static String VER="Verify";
	public final static String REGR="Regression";
	public final static String CLOSED="Closed";
}
