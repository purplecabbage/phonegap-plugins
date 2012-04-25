
var PushNotification = function() {
	
}

// call this to register for push notifications
PushNotification.prototype.register = function(success, fail, options) {
    cordova.exec(success, fail, "PushNotification", "registerAPN", options);
};

// call this to notify the plugin that the device is ready
PushNotification.prototype.startNotify = function(notificationCallback) {
    cordova.exec(null, null, "PushNotification", "startNotify", []/* BUG - dies on null */);
};

// use this to log from JS to the Xcode console - useful!
PushNotification.prototype.log = function(message) {
    cordova.exec(null, null, "PushNotification", "log", [{"msg":message,}]);
};


cordova.addConstructor(function() 
					   {
					   if(!window.plugins)
					   {
					   window.plugins = {};
					   }
					   
					   
					   if (!window.Cordova) {
					   window.Cordova = cordova;
					   };
					   
					   
					   window.plugins.pushNotification = new PushNotification();
					   });