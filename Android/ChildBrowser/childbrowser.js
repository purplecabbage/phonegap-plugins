/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2010, IBM Corporation
 */

/**
 * Constructor
 */
function ChildBrowser() {
}

/**
 * Display a new browser with the specified URL.
 * This method loads up a new web view in a dialog.
 *
 * @param url           The url to load
 */
ChildBrowser.prototype.showWebPage = function(url) {
    PhoneGap.exec(null, null, "ChildBrowser", "showWebPage", [url]);
};

/**
 * Close the browser opened by showWebPage.
 */
ChildBrowser.prototype.close = function() {
    PhoneGap.exec(null, null, "ChildBrowser", "close", []);
};

/**
 * Display a new browser with the specified URL.
 * This method starts a new web browser activity.
 *
 * @param url           The url to load
 * @param usePhoneGap   Load url in PhoneGap webview [optional]
 */
ChildBrowser.prototype.openExternal = function(url, usePhoneGap) {
    PhoneGap.exec(null, null, "ChildBrowser", "openExternal", [url, usePhoneGap]);
};

/**
 * Method called when the child browser is closed.
 */
ChildBrowser.prototype._onClose = function() {
    if (typeof window.plugins.ChildBrowser.onClose === "function") {
        window.plugins.ChildBrowser.onClose();
    }
};

/**
 * Method called when the child browser location changes.
 * @param {DOMString} newLoc the url that is being loaded
 */
ChildBrowser.prototype._onLocationChange = function(newLoc) {
    if (typeof window.plugins.ChildBrowser.onLocationChange === "function") {
        window.plugins.ChildBrowser.onLocationChange(newLoc);
    }
};

ChildBrowser.install = function() {
    
}

/**
 * Load ChildBrowser
 */
PhoneGap.addConstructor(function() {
    PhoneGap.addPlugin("childBrowser", new ChildBrowser());
});
