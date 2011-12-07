/**
 *  
 * @return Object literal singleton instance of DatePicker
 */
var DatePicker = function() {
	
};

DatePicker.prototype.showDateOrTime = function(action,successCallback, failureCallback) {
	 return PhoneGap.exec( 
	 successCallback,    //Success callback from the plugin
	 failureCallback,     //Error callback from the plugin
	 'DatePickerPlugin',  //Tell PhoneGap to run "DatePickerPlugin" Plugin
	 action,              //Tell plugin, which action we want to perform
	 []);        //Passing list of args to the plugin
};

/**
 * Enregistre une nouvelle bibliothèque de fonctions
 * auprès de PhoneGap
 **/

PhoneGap.addConstructor(function() {
    //Register the javascript plugin with PhoneGap
    PhoneGap.addPlugin('datePickerPlugin', new DatePicker());
    
  //Register the native class of plugin with PhoneGap
    PluginManager.addService("DatePickerPlugin",
                         "com.ngapplication.plugin.DatePickerPlugin");
});