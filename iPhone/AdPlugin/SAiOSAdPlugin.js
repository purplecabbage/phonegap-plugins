/*     

Cordova v1.5.0 Support added 2012 @RandyMcMillan
README.md for install notes

*/

/**
 * Constructor
 */
function SAiOSAdPlugin()
{
}

/**
 * show - true to show the ad, false to hide the ad
 */
SAiOSAdPlugin.prototype.orientationChanged = function()
{
    //PhoneGap.exec("SAiOSAdPlugin.orientationChanged", window.orientation);
    Cordova.exec("SAiOSAdPlugin.orientationChanged", window.orientation);
}

/**
 * show - true to show the ad, false to hide the ad
 */
SAiOSAdPlugin.prototype.showAd = function(show)
{
    //PhoneGap.exec("SAiOSAdPlugin.showAd", show);
    Cordova.exec("SAiOSAdPlugin.showAd", show);
}

/**
 * atBottom - true to put the ad at the bottom, false to put the ad at the top
 */
SAiOSAdPlugin.prototype.prepare = function(atBottom)
{
	if (!atBottom) {
		atBottom = false;
	}
    
	//PhoneGap.exec("SAiOSAdPlugin.prepare", atBottom);
	Cordova.exec("SAiOSAdPlugin.prepare", atBottom);
    
}

/**
 * Install function
 */
SAiOSAdPlugin.install = function()
{
	if ( !window.plugins ) 
		window.plugins = {}; 
	if ( !window.plugins.iAdPlugin ) 
		window.plugins.iAdPlugin = new SAiOSAdPlugin();
	
	
}


/**
 * Add to PhoneGap/Cordova constructor
 */
//PhoneGap.addConstructor(SAiOSAdPlugin.install);
Cordova.addConstructor(SAiOSAdPlugin.install);