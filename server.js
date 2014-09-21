var express = require('express'),
	app = express();

app.get('/',function(req,res){
	res.sendfile('./html/henry.html');
});

app.use(express.static('./public'));

app.listen(8080);
