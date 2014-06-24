function Downloader() {}

Downloader.prototype.downloadFile = function(fileUrl, params, win, fail) {
	//Make params hash optional.
	if (!fail) win = params;
	cordova.exec(win, fail, "Downloader", "downloadFile", [fileUrl, params]);
};

if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.tts) {
    window.plugins.downloader = new Downloader();
}
