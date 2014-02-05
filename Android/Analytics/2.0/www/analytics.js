/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2006-2011 Worklight, Ltd.
 */


/**
 * Constructor
 */
var Analytics = function () {};

/**
 * Initialize Google Analytics configuration
 *
 * @param accountId			The Google Analytics account id
 * @param successCallback	The success callback
 * @param failureCallback	The error callback
 */
Analytics.prototype.start = function(accountId, successCallback, failureCallback) {
	return cordova.exec(
				successCallback,
				failureCallback,
				'GoogleAnalyticsTracker',
				'start',
				[accountId]);
};

/**
 * Send a page view on Google Analytics
 * @param key				The name of the tracked item (can be a url or some logical name).
 * 							The key name will be presented in Google Analytics report.
 * @param successCallback	The success callback
 * @param failureCallback	The error callback
 */
Analytics.prototype.sendView = function(key, successCallback, failureCallback) {
	return cordova.exec(
				successCallback,
				failureCallback,
				'GoogleAnalyticsTracker',
				'sendView',
				[key]);
};

/**
 * Track an event on Google Analytics
 * @param category			The name that you supply as a way to group objects that you want to track
 * @param action			The name the type of event or interaction you want to track for a particular web object
 * @param label				Provides additional information for events that you want to track (optional)
 * @param value				Assign a numerical value to a tracked page object (optional)

 * @param successCallback	The success callback
 * @param failureCallback	The error callback
 */

Analytics.prototype.sendEvent = function(category, action, label, value, successCallback, failureCallback){
	return cordova.exec(
				successCallback,
				failureCallback,
				'GoogleAnalyticsTracker',
				'sendEvent',
				[
				    category,
				    action,
				    typeof label === "undefined" ? "" : label,
				    (isNaN(parseInt(value,10))) ? 0 : parseInt(value, 10)
				]);
};

/**
 * Load Analytics
 */

if(!window.plugins) {
    window.plugins = {};
}

if (!window.plugins.analytics) {
    window.plugins.analytics = new Analytics();
}
