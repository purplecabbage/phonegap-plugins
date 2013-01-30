/*
 	Author: Vishal Rajpal
 	Filename: ZipPlugin.js
 	Created Date: 21-02-2012
 	Modified Date: 30-01-2013
*/

var ExtractZipFilePlugin = function(){
}

cordova.addConstructor(function(){
    if(!window.plugins) window.plugins = {};
    window.plugins.extractZipFile = new ExtractZipFilePlugin();
});

ExtractZipFilePlugin.prototype.extractFile = function(file, destination, successCallback, errorCallback) 
{
    return cordova.exec(successCallback, errorCallback, "ZipPlugin", "extract", [file, destination]);
};