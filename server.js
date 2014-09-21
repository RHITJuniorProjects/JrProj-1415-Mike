var express = require('express'),
	app = express();

app.get('/',function(req,res){
	res.sendFile(__dirname+'/html/henry.html');
});

app.get('/henry.js',function(req,res){
	res.sendFile(__dirname+'/js/henry.js');
});

app.use(express.static('./public'));

app.listen(8080);
