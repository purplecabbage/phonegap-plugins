var EmailProvider = function() {};

EmailProvider.prototype.send = function(email, subject, content, successCallback) {

    function success(args) {
        successCallback(args);
    }
    
	return PhoneGap.exec(function(args) {
		success(args);
	}, function(args) {
		fail(args);
	}, 'EmailProvider', 'send', [email, subject, content]);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('emailprovider', new EmailProvider());
	PluginManager.addService("EmailProvider","com.phonegap.plugin.EmailProvider");
});