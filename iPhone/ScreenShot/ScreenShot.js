/*
 *  This code is adapted from the work of Michael Nachbaur 
 *  by Simon Madine of The Angry Robot Zombie Factory
 *  2010-05-04
 *  MIT licensed
*/

/**
 * This class exposes the ability to take a Screenshot to JavaScript
 * @constructor
 */
function Screenshot() {
}

/**
 * Save the screenshot to the user's Photo Library
 */
Screenshot.prototype.saveScreenshot = function( returnBase64, successCallbackString) {
    PhoneGap.exec("Screenshot.saveScreenshot", returnBase64, successCallbackString );
};

Screenshot.prototype.saveScreenshotAsFile = function( fileName, successCallbackString, returnBase64 ) {
    PhoneGap.exec("Screenshot.saveScreenshotAsFile", fileName, successCallbackString, returnBase64 );
};

PhoneGap.addConstructor(function() 
{
	if(!window.plugins)
	{
		window.plugins = {};
	}
    window.plugins.screenShot = new Screenshot();
});
