var contactView = {
    show: function(success, fail){
        cordova.exec(function(args) {
    		success(args)
    	}, function(args) {
    		fail(args);
    	}, "ContactView", "show", []);
    }
}

// optionally add the object to windows.plugins
// windows.plugins.contactView = contactView;