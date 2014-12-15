
var projectID;
var linesOfCodeArray = [];
var nameArray = [];
var milestoneNameArray = [];
var milestonePercentArray = [];
var projectHoursArray = [];
var projectNameArray = [];
var projectTaskArray = [];
var taskNameArray = [];
var taskPercentArray = [];
var getLinesOfCode;
var getNameAndDraw;
var pieChartDrawer;
var getMilestoneData;
var getProjectData;

function drawProjectStuff (fb) {

fb.child("projects").on('value', function(snapshot) {
    var projectArray = [];

    console.log("function called");
    
    for(var item in snapshot.val()){
        //console.log(item);
        projectArray.push(item);
    }

    for(i = 0; i<projectArray.length; i++){
       //console.log("milestone id " + milestoneNameArray[i]);
        getProjectData(projectArray[i], projectArray);
     }

});

function getProjectData(item, array){
    fb.child("projects/" + item +"/hours_percent").on('value',function(snapshot){
       // console.log(array);
        projectHoursArray.push(snapshot.val());

        if(array.length == projectHoursArray.length){
           fb.child("projects/" + item +"/name").on('value',function(snapshot){
            //console.log(item);
            projectNameArray.push(snapshot.val());
            //console.log(snapshot.val());
            });
        }
         if(array.length == projectNameArray.length){
           fb.child("projects/" + item +"/task_percent").on('value',function(snapshot){
            projectTaskArray.push(snapshot.val());


         });
        }
    projectDrawerHours(projectNameArray, projectHoursArray);
    projectDrawerTask(projectNameArray, projectTaskArray);


    });

};

//by hours
function projectDrawerHours(name, hours) {
   // console.log("xxxxxx");
    $('#projContainer1').highcharts({
        chart: {
            type: 'column',
            margin: 75,
            options3d: {
                enabled: false,
                alpha: 10,
                beta: 25,
                depth: 70
            }
        },
        title: {
            text: 'Progress of Projects',
            style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            opposite: false,
            tickInterval: 20,
            max: 100,
            title: {
                text: 'Percent Complete'
            }
        },
        series: [{
            name: 'Percent Complete by Hours',
            data: hours
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};

//by task
function projectDrawerTask(name, taskData) {
    $('#projContainer2').highcharts({
        chart: {
            type: 'column',
            margin: 75,
            options3d: {
                enabled: false,
                alpha: 10,
                beta: 25,
                depth: 70
            }
        },
        title: {
            text: 'Project completeness based on Task',
            style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            opposite: false,
            tickInterval: 20,
            max: 100,
            title: {
                text: 'Percent Complete'
            }
        },
        series: [{
            name: 'Percent Complete',
            data: taskData
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};

};


function drawtaskStuff (projID, mileID, fb) {
  projectID = projId.toString();
  milestoneID = mileID.toString();

  fb.child("projects/" + projectID + "/milestone/" + milestoneID + "/tasks").on('value', function(snapshot) {
    var taskIDArray = [];
    for(var item in snapshot.val()){
        taskIDArray.push(item);
    }
    for(i =0; i < taskIDArray.length; i++){
        getTaskData(taskIDArray[i], taskIDArray);
    }
    


});


function getTaskData(item, array){
fb.child(item +"/name").on('value',function(snapshot){
        taskNameArray.push(snapshot.val());

        if(array.length == taskNameArray.length){
           fb.child(item +"/percent_complete").on('value',function(snapshot){
            taskPercentArray.push(snapshot.val());
            console.log(snapshot.val());
            });
        }

        taskDrawer(taskNameArray, taskPercentArray);

});
};

function taskDrawer(name, percent_complete){
      $('#taskContainer').highcharts({
        chart: {
            type: 'column',
            options3d: {
                enabled: false,
                alpha: 10,
                beta: 25,
                depth: 70
            }
        },
        title: {
            text: 'Progress of Tasks',
            style: { "color": "#333333", "fontSize": "30px"},
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            alternate:'#F0F0F0',
            opposite: false,
            tickInterval: 20,
            margin: 120,
            max: 120,
            labels: {
                    formatter: function() {
                        var value = change[this.value];
                        return value !== 'undefined' ? value : this.value;
                    }
            },
            title: {
                text: 'Task Stage',
                style: {"font-family": "Arial", "font-weight": "bold", "color": "#333333"}  
            }
        },
        series: [{
            name: 'Percent Complete',
            data: percent_complete
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};


};

function drawMilestoneStuff(projId, fb){		
	
projectID = projId.toString();


//fills workerArray and linesOfCodeArray
fb.child("projects/" + projectID +"/members").on('value', function (snapshot) {
	//var userData = snapshot.val();
	var workerArray = [];

	for(var item in snapshot.val()){
	//	console.log(item);
		workerArray.push(item);
	}

	for(i = 0; i<workerArray.length; i++){
		getLinesOfCode(workerArray[i], workerArray);
	}
});

fb.child("projects/" + projectID + "/milestones").on('value', function(snapshot) {
    var milestoneArray = [];

    for(var item in snapshot.val()){
        milestoneArray.push(item);
    }

    for(i = 0; i<milestoneArray.length; i++){
       console.log("milestone id " + milestoneNameArray[i]);
        getMilestoneData(milestoneArray[i],milestoneArray,projectID);
        }

});





function getLinesOfCode(item, array){
						fb.child("users/" + item + "/projects/" + projectID + "/total_lines_of_code").on('value', function(snapshot){
							linesOfCodeArray.push(snapshot.val());
	
							if(array.length == linesOfCodeArray.length){
								for(i = 0; i<array.length; i++){
									getNameAndDraw(array[i], array, linesOfCodeArray);
								}	
							}					
						});
};

function getMilestoneData(item, array, projectID){
    fb.child("projects/" + projectID + "/milestones/" + item + "/name").on('value', function(snapshot){
        milestoneNameArray.push(snapshot.val());
        // console.log("sbao" +snapshot.val());
        // console.log("projectID"+projectID);
       
        
       if(array.length == milestoneNameArray.length){
            fb.child("projects/" + projectID + "/milestones/" + item + "/task_percent").on('value', function(snapshot){
             milestonePercentArray.push(snapshot.val());
        // console.log("sbao" +snapshot.val());
        // console.log("projectID"+projectID);
            
     });
    }
    milestoneDrawer(milestoneNameArray,milestonePercentArray);  
    });

};



// function getMilestoneName(item, array, projectID){
//     fb.child("projects/" + projectID + "/milestones/" + item + "/task_percent").on('value', function(snapshot){
//         milestonePercentArray.push(snapshot.val());
//         // console.log("sbao" +snapshot.val());
//         // console.log("projectID"+projectID);
//         milestoneDrawer(milestonePercentArray);


//     });

// };

function getNameAndDraw(current, userArray, linesArray){
						var totalArray = [];
						
						fb.child("users/" + current + "/name").on('value', function(snapshot){
							nameArray.push(snapshot.val());
							
							if(nameArray.length == userArray.length){
								for(i = 0; i<linesArray.length; i++){
									totalArray.push(new Array(nameArray[i], linesArray[i]));
                                   
								}
								
								pieChartDrawer(totalArray);

							}
						});
};
						

/*
fb.child("users/simplelogin:25/projects/" + projectID + "/total_lines_)of_code").on('value', function (snapshot) {
  //var userData = snapshot.val();
  console.log(snapshot.val());
}, function (errorObject) {
  console.log('The read failed: ' + errorObject.code);
});
*/



//console.log(projectLines);


function milestoneDrawer(name, data) {
    console.log(name);
    $('#mileContainer').highcharts({
        chart: {
            type: 'column',
            margin: 75,
            options3d: {
                enabled: false,
                alpha: 10,
                beta: 25,
                depth: 70
            }
        },
        title: {
            text: 'Progress of Milestones',
			style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            opposite: false,
			tickInterval: 20,
			max: 100,
            title: {
                text: 'Percent Complete'
            }
        },
        series: [{
            name: 'Percent Complete',
            data: data
        }],
		colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};


var change = {
    20: 'New',
    40: 'Implementation',
    60: 'Testing',
    80: 'Verifying',
    100: 'Regression',
	120: 'Closed'
};

// $(function () {
//     $('#taskContainer').highcharts({
//         chart: {
//             type: 'column',
//             options3d: {
//                 enabled: false,
//                 alpha: 10,
//                 beta: 25,
//                 depth: 70
//             }
//         },
//         title: {
//             text: 'Progress of Tasks',
// 			style: { "color": "#333333", "fontSize": "30px"},
//         },
//         plotOptions: {
//             column: {
//                 depth: 25
//             }
//         },
//         xAxis: {
//             categories: ["Task 1", "Task 2", "Task 3", "Task 4", "Task 5"]
//         },
//         yAxis: {
// 			alternate:'#F0F0F0',
//             opposite: false,
// 			tickInterval: 20,
// 			margin: 120,
// 			max: 120,
// 			labels: {
// 					formatter: function() {
// 						var value = change[this.value];
// 						return value !== 'undefined' ? value : this.value;
// 					}
// 			},
//             title: {
//                 text: 'Task Stage',
// 				style: {"font-family": "Arial", "font-weight": "bold", "color": "#333333"}	
//             }
//         },
//         series: [{
//             name: 'Percent Complete',
//             data: [120, 120, 20, 60, 80]
//         }],
// 		colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
// 				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
//     });
// });

function pieChartDrawer(temp){
						$(function () {
							$('#linesOfCode').highcharts({
								chart: {
									plotBackgroundColor: null,
									plotBorderWidth: 1,//null,
									plotShadow: false
								},
								title: {
									text: 'Breakup of Committed Lines of Code for Project'
								},
								plotOptions: {
									pie: {
										allowPointSelect: true,
										cursor: 'pointer',
										dataLabels: {
											enabled: true,
											format: '<b>{point.name}:</b> {point.y} lines',
											style: {
												color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
											}
										}
									}
								},
								series: [{
									type: 'pie',
									name: 'Line Breakup',
									data: temp
								}]
							});
						});
};
};