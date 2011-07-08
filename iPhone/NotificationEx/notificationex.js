/**
 * This class provides extended access to notifications on the device. (iOS)
 * In PhoneGap.plist/Plugins, add this mapping (key:NotificationEx, value:NotificationEx)
 */
NotificationEx = function() {
};

// iPhone only
NotificationEx.prototype.loadingStart = function(options) {
    PhoneGap.exec(null, null, "NotificationEx","loadingStart", [options]);
};
// iPhone only
NotificationEx.prototype.loadingStop = function() {
    PhoneGap.exec(null, null, "NotificationEx","loadingStop", []);
};

PhoneGap.addConstructor(function() {
    if (typeof navigator.notificationEx == "undefined") navigator.notificationEx = new NotificationEx();
});
