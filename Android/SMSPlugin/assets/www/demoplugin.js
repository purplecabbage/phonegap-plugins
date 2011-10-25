var DemoPlugin = function () {
	
};

DemoPlugin.prototype.sendSMS = function (successCallback, failureCallback, phone, message) {	
	return PhoneGap.exec(successCallback, failureCallback, 'DemoPlugin', "SendSMS", [phone, message]);
}