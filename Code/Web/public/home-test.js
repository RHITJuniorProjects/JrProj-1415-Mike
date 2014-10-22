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
		colors: ['#ff69b4', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
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
		colors: ['#ff69b4', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
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
		colors: ['#ff69b4', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
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
		colors: ['#ff69b4', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
				'#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
});