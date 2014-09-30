public static void greet(Person reader){
	if (reader.isTeammate()){
		System.out.println("Howdy Pardner!");
		}
	else if (reader.isPM()){
		System.out.println("I've done my work!");
		}
	else if (reader.isProf()){
		System.out.println("Please give me an A!");
		}
	else{
		System.out.println("Who are you and why are you reading this?");
		}
}