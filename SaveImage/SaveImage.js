/*
 *  This code is adapted from the work of Michael Nachbaur
 *  and by Simon Madine of The Angry Robot Zombie Factory
 *  MyFreeWeb 2010
 *  MIT licensed
*/

/**
 * This class exposes the ability to save an image to Javascript
 * @constructor
 */
function SaveImage() {
}

/**
 * Save an image to user's Photo Library
 */
SaveImage.prototype.saveImage = function(data) {
    PhoneGap.exec("SaveImage.saveImage", data);
};

PhoneGap.addConstructor(function() 
{
	if(!window.plugins)
	{
		window.plugins = {};
	}
    window.plugins.SaveImage = new SaveImage();
});
