if (typeof selector === "undefined") { selector = {}; }

if(selector.addLibs === undefined){
	selector.addLibs = function(){
		var node = document.createElement("script");
		node.src = "http://jqueryjs.googlecode.com/files/jquery-1.3.2.min.js";
		document.body.appendChild(node);
		selector.nodeSelector();
	};
	
	selector.nodeSelector = function() {
		if (window.console === undefined) { window.console = {log:function(){}}; }	
		var mouseover = function(ev) {
		    ev.stopPropagation();
		    var e = $(ev.target);
		    if (typeof e.css("outline") != "undefined") {
		        e.data("saved", {"outline" : e.css("outline")});
		        e.css("outline", "blue solid medium");
		    } else {
		        e.data("saved", {"backgroundColor" : e.css("backgroundColor")});
		        e.css("backgroundColor", "#0cf");
		    }
		};	
			

		var mouseout = function(ev) {
		    ev.stopPropagation();
		    var e = $(ev.target);
		    save = e.data("saved");
		    if (typeof(save) == "undefined") { return; }
		    e.removeData("saved");
		    for (var i in save) {
		        e.css(i, save[i]);
		    }
		};
		   
		    
		var click =  function (ev) {
			ev.stopImmediatePropagation();
		    ev.preventDefault(); 
		    ev.stopPropagation();
		    ev.cancelBubble = true;   
		    ev.returnValue = false;
		    var e = $(ev.target);
		    var id = getId(ev.target);
		    var name = getName(ev.target);
		    var xpath = getXpath(ev.target);
		    var frameName = getFrameNameByElement(ev.target);
		    
		    console.log(frameName);
		    console.log("xpath="+xpath);
		    
		    if(frameName === undefined){
		    	var node = $("#hover");
		 	    if (node.size() === 0)  {
		 	    	$(document.body).append("<div id='hover'></div>");
		 	        node = $("#hover");
		 	        node
		 	        .css("position", "absolute")
		 	        .css("display", "inline")
		 	        .css('backgroundColor', "white")
		 	        .css('padding', '4px')
		 	        .css('width', 'auto') 
		 	        .css("zIndex", 99999)
		 	        .css('font-size','14px')
		 	        .click(function(ev) { ev.stopPropagation(); });
		 	    }
			        node.html("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\" style=\"border-collapse:collapse;word-break:break-all; word-wrap:break-all;\">" +
			        		"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">id</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+id+"</td>" +
		            		"</tr>" +
		            		"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">name</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+name+"</td>" +
		            		"</tr>" +
		            		"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">xpath</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+xpath+"</td>" +
		            		"</tr>" +
			        		"</table>"); 
			        node.animate({
		 	        'top' : (e.offset().top) + "px",
		 	        'left': (e.offset().left) + "px"
		 	    }, 250);
		    } else{
		    	var node = $(window.frames[frameName].document.body).find("#hover");
		    	if (node.size() === 0)  {
		 	    	$(window.frames[frameName].document.body).append("<div id='hover'></div>");
		 	        node = $(window.frames[frameName].document.body).find("#hover");
		 	        node
		 	        .css("position", "absolute")
		 	        .css("display", "inline")
		 	        .css('backgroundColor', "white")
		 	        .css('padding', '4px')
		 	        .css('width', 'auto') 
		 	        .css("zIndex", 99999)
		 	        .css('font-size','14px')
		 	        .click(function(ev) { ev.stopPropagation(); });
		    	}
		    		node.html("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\" style=\"border-collapse:collapse;word-break:break-all; word-wrap:break-all;\">" +
		    				"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">frameName</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+frameName+"</td>" +
		            		"</tr>" +
		            		"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">id</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+id+"</td>" +
		            		"</tr>" +
		            		"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">name</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+name+"</td>" +
		            		"</tr>" +
		            		"<tr>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">xpath</td>" +
		            		"<td height=\"35\" width=\"50%\" style=\"border:1px solid black;vertical-align:center;color:black;\">"+xpath+"</td>" +
		            		"</tr>" +
		        			"</table>"); 	 	    
		    		node.animate({
		     	        'top' : ("10") + "px",
		     	        'left': ("10") + "px"
		     	    }, 250);
		    }
		};


		var all = $("*");

		$("*").each(function(){
			if($(this).is("frame")||$(this).is("iframe")){
				var framename = $(this)[0].name;
				var children = $(window.frames[framename].document).find("*");
				children.keydown(keydown);
				all.push(children);
			}
		});


		all.each(function() {
			$(this)
			.mouseover(mouseover)
			.mouseout(mouseout)
			.click(click);
			});
		    
		var keydown = function(e) {
		    if (e.keyCode === undefined && e.charCode !== undefined) { e.keyCode = e.charCode; }
		    if (e.keyCode == 27) {
		    	$("*").each(function() {
		    		$(this)
		            .mouseout()
		            .unbind("mouseover", mouseover)
		            .unbind("mouseout", mouseout)
		            .unbind("click", click)
		            .mouseout();
		    		if($(this).is("frame")||$(this).is("iframe")){
		    			var framename = $(this)[0].name;
		    			var children = $(window.frames[framename].document).find("*");
		    			children.each(function() {
		    				$(this)
		    		        .mouseout()
		    		        .unbind("mouseover", mouseover)
		    		        .unbind("mouseout", mouseout)
		    		        .unbind("click", click)
		    		        .mouseout();
		    			});
		    			$(window.frames[framename].document).find("#hover").remove();
		    			$(window.frames[framename].document).unbind("keydown", keydown);
		    		}
		        });
		    	$("#hover").remove();
		    	$(document).unbind("keydown", keydown);
		    	selector.addLibs = undefined;
		    }
		};    
		    
		$(document).keydown(keydown);


		function getFrameNameByElement(e) {
			var frame;
			var frameName;
		    $("frame").each(function(){
		        if(e.ownerDocument === this.contentWindow.document) {
		            frame = this;
		            frameName = frame.name;
		        }
		    });
		    $("iframe").each(function(){
		        if(e.ownerDocument === this.contentWindow.document) {
		            frame = this;
		            frameName = frame.name;
		        }
		    });
		    return frameName;
		}

		function getId(e) {
			return e.id;
		}

		function getName(e) {
			return e.name;
		}


		function getXpath(e) {
		    var xpath = "";
		    var oldE = e;
		    while (e.nodeName.toLowerCase() != "html") {
		        var node = e.nodeName;
		        node = node.toLowerCase(); 
		        var id = e.id;
		        if (id !== undefined && id !== null && id !== "") {
		            xpath = "//" + node + "[@id='" + id + "']" + xpath;
		            break;
		        }
		        var parent = e.parentNode;
		        var children = $(parent).children(node);
		        if (children.size() > 1) {
		            var good = false;
		            children.each(function(i) {
		                if (this == e) {
		                    node = node + "[" + (i+1) + "]";
		                    good = true;
		                    return false;
		                }
		            });
		            if (! good) {
		                console.log("Can't find child, something is wrong with your dom : " + node);
		                return FALSE;
		            }
		        }
		        xpath = "/" + node + xpath;
		        e = parent;
		    }
		    if (xpath.substring(0, 2) != "//") { xpath = "/html" + xpath; }
		    return xpath;
		}
	};
	
	selector.addLibs();
}



