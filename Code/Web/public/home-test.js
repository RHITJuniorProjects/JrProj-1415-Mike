
var projectID;
var linesOfCodeArray = [];
var nameArray = [];
var getLinesOfCode;
var getNameAndDraw;
var pieChartDrawer;


function drawStuff(projId){		
	
var fb = new Firebase("https://henry-test.firebaseIO.com");
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

$(function () {
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
            text: 'Project completeness based on hours',
			style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: ["Project 1", "Project 2", "Project 3"]
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
			data:[{
					y:50,
					ownURL:'http://www.google.com'
				},{
					y:33,
					ownURL:'http://www.espn.com'
				},{
					y:0,
					ownURL:'http://www.weater.com'
				}]
        }],
		colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
});

$(function () {
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
            text: 'Project completeness based on milestones',
			style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: ["Project 1", "Project 2", "Project 3"]
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
            data: [60, 25, 0]
        }],
		colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
});

$(function () {
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
            categories: ["Milestone 1", "Milestone 2", "Milestone 3", "Milestone 4"]
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
            data: [100, 95, 50, 10]
        }],
		colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
});


var change = {
    20: 'New',
    40: 'Implementation',
    60: 'Testing',
    80: 'Verifying',
    100: 'Regression',
	120: 'Closed'
};

$(function () {
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
            categories: ["Task 1", "Task 2", "Task 3", "Task 4", "Task 5"]
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
            data: [120, 120, 20, 60, 80]
        }],
		colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
});

function pieChartDrawer(temp){
						$(function () {
							$('#linesOfCode1').highcharts({
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