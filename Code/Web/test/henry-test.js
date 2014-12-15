
var HenryTest = TestCase("HenryTest");

HenryTest.prototype.testMakeSelect = function(){
	var callbackCalled = false;
	var select = makeSelect([1,2,3,4,5],2,function(){
		callbackCalled = true;
	});
	assertEquals("test select default value",select.val(),2);
	var i = 1;
	select.children('option').each(function(index,option){
		assertEquals("test option values and order",String(i),option.value);
		i++;
	});
	assertEquals("assert select callback not called before change",callbackCalled,false);
	select.change();
	assertEquals("assert select callback called after change",callbackCalled,true);
};

HenryTest.prototype.testMakeProgressBar = function(){
	var percentRef = new Firebase('percentRef'),
		container = makeProgressBar('my-progress-class','label',percentRef),
		progress = container.children('.progress'),
		span = progress.children('.meter'),
		h4 = container.children('h4');
	percentRef.set(30);
	percentRef.flush();
	assertEquals('check if progress bar is correct width','width: 30%;',span.attr('style'));
	assertEquals('check progress bar label',h4.text(),'label');
	assertEquals('check progress bar class',progress.attr('class'),"progress my-progress-class");
	percentRef.set(60);
	percentRef.flush();
	assertEquals('check if progress bar width changed',span.attr('style'),'width: 60%;');
};

HenryTest.prototype.testViewMyTasks = function(){
	// Instantiate a firebase project with some tasks assigned to a specific user
	var myTasksRef Firebase('myTasks'),
		container = makeProgressBar('my-progress-class','label',myTasksRef),
		progress = container.children('.progress'),
		span = progress.children('.meter'),
		h4 = container.children('h4');

	// Set tasks to view
	myTasksRef.set();
	myTasksRef.flush();
	// See if the tasks are there
	assertEquals('check if the tasks are shown','width: 30%;',span.attr('style'));
	assertEquals('check if one of the tasks has the right label',h4.text(),'label');
	assertEquals('check the Description of one of the tasks',progress.attr('class'),"progress my-progress-class");

	// Add a new task
	myTasksRef.set();
	myTasksRef.flush();
	// See if the new task is there
	assertEquals('check if new task is shown',span.attr('style'),'width: 60%;');
};
