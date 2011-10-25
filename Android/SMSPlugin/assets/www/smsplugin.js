var SmsPlugin = function () {};

SmsPlugin.prototype.send = function (phone, message, successCallback, failureCallback) {    
    return PhoneGap.exec(successCallback, failureCallback, 'SmsPlugin', "SendSMS", [phone, message]);
};

PhoneGap.addConstructor(function() {
    PhoneGap.addPlugin("sms", new SmsPlugin());
});
