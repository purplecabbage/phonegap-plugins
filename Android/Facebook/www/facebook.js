/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2010, IBM Corporation
 */


(function(){
	/**
	 * Constructor
	 */
	function Facebook() {}

	/**
	 * Display a new browser with the specified URL.
	 *
	 * @param url           The url to load
	 * @param usePhoneGap   Load url in PhoneGap webview [optional]
	 */
	Facebook.prototype.authorize = function(url) {
	    PhoneGap.exec(null, null, "FacebookAuth", "authorize", [url]);	
	};

	/**
	 * Load ChildBrowser
	 */
	PhoneGap.addConstructor(function() {
	    PhoneGap.addPlugin("facebook", new Facebook());
	    PluginManager.addService("FacebookAuth", "com.phonegap.plugins.facebook.FacebookAuth");
	});

})();