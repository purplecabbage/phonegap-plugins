/**
 * Phonegap File Upload plugin
 * Copyright (c) Matt Kane 2010
 *
 */
var FileUploader = function() { 

}


/**
 * Given a content:// uri, uploads the file to the server as a multipart/mime request
 *
 * @param server URL of the server that will receive the file
 * @param file content:// uri of the file to upload
 * @param fileKey Object with key: value params to send to the server
 * @param params Parameter name of the file
 * @param fileName Filename to send to the server. Defaults to image.jpg
 * @param mimeType Mimetype of the uploaded file. Defaults to image/jpeg
 * @param callback Success callback. Also receives progress messages during upload.
 * @param fail Error callback
 */
FileUploader.prototype.uploadByUri = function(server, file, params, fileKey, fileName, mimeType, callback, fail) {
	
    return PhoneGap.exec(function(args) {
        callback(args);
    }, function(args) {
		if(typeof fail == 'function') {
	        fail(args);
		}
    }, 'FileUploader', 'uploadByUri', [server, file, params, fileKey, fileName, mimeType]);
};

/**
 * Given absolute path, uploads the file to the server as a multipart/mime request
 *
 * @param server URL of the server that will receive the file
 * @param file Absolute path of the file to upload
 * @param fileKey Object with key: value params to send to the server
 * @param params Parameter name of the file
 * @param fileName Filename to send to the server. Defaults to image.jpg
 * @param mimeType Mimetype of the uploaded file. Defaults to image/jpeg
 * @param callback Success callback. Also receives progress messages during upload.
 * @param fail Error callback
 */
FileUploader.prototype.upload = function(server, file, params, fileKey, fileName, mimeType, callback, fail) {
	
    return PhoneGap.exec(function(args) {
		if(typeof callback == 'function') {
			callback(args);
		}
    }, function(args) {
		if(typeof fail == 'function') {
	        fail(args);
		}
    }, 'FileUploader', 'upload', [server, file, params, fileKey, fileName, mimeType]);
};


FileUploader.Status = {
	PROGRESS: "PROGRESS",
	COMPLETE: "COMPLETE"
}

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('fileUploader', new FileUploader());
	PluginManager.addService("FileUploader","com.beetight.FileUploader");
});