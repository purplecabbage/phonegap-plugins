var ContactView = function() {};

ContactView.prototype.show = function(element) {

    function success(args) {
        var el = document.getElementById(element);
        el.value = args;    
    }
    
    function fail(args) {
    	alert("fail "+args)
    }

	return PhoneGap.exec(function(args) {
	        success(args);
	    }, function(args) {
	        fail(args);
	    }, 'ContactView', '', []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('contactView', new ContactView());
	PluginManager.addService("ContactView","com.rearden.ContactView");
});