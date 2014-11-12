package rhit.jrProj.henry.firebase;

//TODO

public class Enums {
	public enum Role {
		developer, lead;

		public static Role parse(String value) {
			if (value.equals("lead")) {
				return Role.lead;
			}
			return Role.developer;
		}
	}

	public enum Category {
		Bug,
	}
}
