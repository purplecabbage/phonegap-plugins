var PushNotification = function() {}

// call this to register for push notifications
PushNotification.prototype.register = function(success, fail, options) {
  PhoneGap.exec(success, fail, "PushNotification", "registerAPN", options);
};

// use this to log from JS to the Xcode console - useful!
PushNotification.prototype.log = function(message) {
  PhoneGap.exec(null, null, "PushNotification", "log", [{"msg":message,}]);
};

if(!window.plugins) window.plugins = {};
window.plugins.pushNotification = new PushNotification();