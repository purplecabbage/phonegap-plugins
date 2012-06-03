/*****************************************************************************
 * 
 * ThumbnailCreator
 * Author: Kerri Shotts, June 1 2012
 *    for: Salman FF, PhoneGap Community
 * 
 * portions from http://www.java2s.com/Code/Android/2D-Graphics/SaveloadBitmap.htm
 * and phonegap-plugins/blob/master/Android/Downloader/Downloader.java (as template)
 * 
 * This plugin will take an incoming image (specified by souceImageUrl) and resize it
 * according to the incoming width and height and quality. It will then save it to 
 * the incoming target url.
 * 
 * Installation: Create a directory named "com.salman.plugins.thumbnailcreator"
 * under the src directory in your project; copy ThumbnailCreator.java to that
 * directory in your project. Register the plugin in the plugins.xml file
 * 
 *  <plugin name="ThumbnailCreator" value="com.salman.plugins.thumbnailcreator.ThumbnailCreator">
 * 
 * Include the .js file in your assets/www directory
 * and then use
 * 
 *     window.plugin.thumbnailCreator.createThumbnail ( 
 *         "/the/full/path/to/the/source/image.jpg",
 *         "/the/full/path/to/the/target/image.jpg",
 *         300, // the new width
 *         200, // the new height
 *         80,  // the jpeg quality
 *         success, // the success function
 *         failure  // the failure function
 *     );
 *     
 * Failure Codes: IO_EXCEPTION (raised for any file related problem),
 *                MALFORMED_URL_EXCEPTION (not passing anything for the source or target)
 *                JSON_EXCEPTION (error in the JSON created by createThumbnail() )
 * Success Code:  OK (image resized, and saved to location.)
 * 
 */
 
 function ThumbnailCreator() {}
 
 ThumbnailCreator.prototype.createThumbnail = function ( theSourceImagePath, theTargetImagePath, newWidth, newHeight, quality, success, failure )
 {
     var params = { "sourceImageUrl" : theSourceImagePath,
                    "targetImageUrl" : theTargetImagePath,
                    "newWidth"       : newWidth || 300,
                    "newHeight"      : newHeight || 300,
                    "quality"        : quality || 80};
     
     cordova.exec (success, failure, "ThumbnailCreator", "createThumbnail", [params]);
 };
 
 cordova.addConstructor (function()
                         {
                             cordova.addPlugin("thumbnailCreator", new ThumbnailCreator());
                             PluginManager.addService ("ThumbnailCreator","com.salman.plugins.thumbnailcreator.ThumbnailCreator");
                         });
 }