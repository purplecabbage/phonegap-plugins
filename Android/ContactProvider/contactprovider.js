var ContactProvider = function() {};

ContactProvider.prototype.add = function(name, email, successCallback, failCallback) {

    function success(args) {
        successCallback(args);
    }
    
    function fail(args) {
    	failCallback(args);
    }

	return PhoneGap.exec(function(args) {
		success(args);
	}, function(args) {
		fail(args);
	}, 'ContactProvider', 'add', [name, email]);
};

ContactProvider.prototype.pick = function(successCallback, failCallback){
	function success(args) {
        successCallback(args);
    }
    
    function fail(args) {
    	failCallback(args);
    }

	return PhoneGap.exec(function(args) {
		success(args);
	}, function(args) {
		fail(args);
	}, 'ContactProvider', 'pick', []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('contactprovider', new ContactProvider());
	PluginManager.addService("ContactProvider","com.phonegap.plugin.contact.ContactProvider");
});