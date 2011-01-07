var ContactView = function() {};

ContactView.prototype.show = function(nameElement, phoneElement) {

    function success(args) {
        nameElement.value = typeof args.name != "undefined" ? args.name : "";
        phoneElement.value = typeof args.phone != "undefined" ? args.phone : "";
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