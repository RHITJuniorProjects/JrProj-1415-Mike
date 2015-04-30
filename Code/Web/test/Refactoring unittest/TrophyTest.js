


(function TestTrophyGets(){
	
	//pick up a trophy here
	var trophyfirebase = new Firebase("https://henry-test.firebaseio.com/trophies/gold");
	var trophy = new Trophy(trophyfirebase);
	
	trophy.getName(function(val){
		if (val === "Gold")
			console.log("name test passed");
		else
			console.log("name test failed");
	});
	
	trophy.getDescription(function(val){
		if (val === "The ultimate gold trophy only for the best")
			console.log("description test passed");
		else
			console.log("description test failed");
	});
	
	trophy.getCost(function(val){
		if (val === 30)
			console.log("cost test passed");
		else
			console.log("cost test failed");
	});
	
	trophy.getImage(function(val){
		if (val === "http://vignette4.wikia.nocookie.net/thelastofus/images/1/10/Gold.png/revision/latest?cb=20130617213754")
			console.log("image test passed");
		else
			console.log("image test failed");
	});
	
}());