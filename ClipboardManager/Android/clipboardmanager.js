/**
 * Phonegap ClipboardManager plugin
 * Omer Saatcioglu 2011
 *
 */

var ClipboardManager = function() { 
}

ClipboardManager.prototype.copy = function(str, success, fail) {
	PhoneGap.execAsync(success, fail, "ClipboardManager", "copy", [str,]);
};

ClipboardManager.prototype.paste = function(success, fail) {
	PhoneGap.execAsync(success, fail, "ClipboardManager", "paste", []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('clipboardManager', new ClipboardManager());
	PluginManager.addService("ClipboardManager","com.saatcioglu.phonegap.clipboardmanager.ClipboardManagerPlugin");
});