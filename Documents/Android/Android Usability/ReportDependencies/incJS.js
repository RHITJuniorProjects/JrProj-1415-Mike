
function popMediaWindow(myHTML, time, filename) {
    var vLeftPos;
    var iWidth;
    var iHeight;
    if (myHTML == 'MatrixViewer.htm') {
        vLeftPos = 0;
        iWidth = screen.availwidth;
        iHeight = screen.availheight;
    }
    else {
        vleftPos = 0;
        iWidth = screen.availWidth;
        iHeight = screen.availheight;
    }
    target = myHTML + '?File=' + escape(filename) + '&startTime=' + time;
    aWindow = window.open(target, "MediaWin", "width = " + iWidth + ", height = " + iHeight + ", left = " + vLeftPos + " , top = 0, status=yes, resizable = yes, scrollbars = yes ");
    aWindow.focus()
    StartTime = time
}
function popScrollWindow(time, filename) {
    vLeftPos = screen.availWidth - 510;
    target = 'mediawin.htm?File=' + escape(filename) + '&startTime=' + time;
    aWindow = window.open(target, "MediaWin", "width = 360, height = 400, left = " + vLeftPos + " , top = 0, status=yes, resizable = yes, scrollbars = yes ");
    aWindow.focus()
    StartTime = time
}

function funcShowHide(theClass,theAnchor) {
//Used to show/hide data details in the report.
//Assumes that .details is first style in CSS, array id 0 (zero)
//Assumes that .colObsMetaData is third style in CSS, array id 2 (two)
	var theRules = new Array();
	var i
	if (theClass=="colObsMetaData") {
		var i = 2 //for logs.htm show/hide
	} else {
		var i = 0 //for .detail on other pages
	}


	if (document.styleSheets[0].cssRules) {
		//For Mozilla
		theRules = document.styleSheets[0].cssRules;
		if (document.styleSheets[0].cssRules[i].style.display=="block") {
			document.styleSheets[0].cssRules[i].style.display="none";
		} else {
			document.styleSheets[0].cssRules[i].style.display="block";
		}
	} else if (document.styleSheets[0].rules) {
		//For Microsoft
		theRules = document.styleSheets[0].rules;
		if (document.styleSheets[0].rules[i].style.display=="block") {
			document.styleSheets[0].rules[i].style.display="none";
		} else {			
			document.styleSheets[0].rules[i].style.display="block";
		}
	}

	var sAnchor = "#" + theAnchor;
	document.location.href=sAnchor;
	}

function funcGrowShrink(theClass,theAnchor) {
//Used to grow/shrink graph images in the report.
	var theRules = new Array();


	if (document.styleSheets[0].cssRules) {
		//For Mozilla
		theRules = document.styleSheets[0].cssRules;
		if (document.styleSheets[0].cssRules[1].style.width=="48em") {
			document.styleSheets[0].cssRules[1].style.width="24em";
			document.styleSheets[0].cssRules[1].style.height="18em";
		} else {
			document.styleSheets[0].cssRules[1].style.width="48em";
			document.styleSheets[0].cssRules[1].style.height="36em";
		}
	} else if (document.styleSheets[0].rules) {
		//For Microsoft
		theRules = document.styleSheets[0].rules;
		if (document.styleSheets[0].rules[1].style.width=="48em") {
			document.styleSheets[0].rules[1].style.width="24em";
			document.styleSheets[0].rules[1].style.height="18em";
		} else {			
			document.styleSheets[0].rules[1].style.width="48em";
			document.styleSheets[0].rules[1].style.height="36em";
		}
	}

	var sAnchor = "#" + theAnchor;
	document.location.href=sAnchor;
	}