;(function(cordova) {

	function StatusBarNotifier() {}

	StatusBarNotifier.prototype.show = function(text, timeOnScreen, callback) {
    if (typeof timeOnScreen === "function") callback = timeOnScreen
	  if (!callback) callback = function() {}
		cordova.exec(callback, callback, "StatusBarNotifier", "getDeviceDetails", [{text: text, timeOnScreen: timeOnScreen}])
	}

 	if (!window.plugins) window.plugins = {}
	window.plugins.deviceDetails = new StatusBarNotifier()

})(window.cordova || window.Cordova || window.PhoneGap);
