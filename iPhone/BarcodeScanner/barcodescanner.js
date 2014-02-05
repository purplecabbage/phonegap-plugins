/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2010, IBM Corporation
 */
var PGBarcodeScanner;

;(function(){
if (!window.Cordova) window.Cordova = window.cordova;
//PhoneGap.addResource("PGBarcodeScanner")

//-------------------------------------------------------------------
PGBarcodeScanner = function() {
}

//-------------------------------------------------------------------
PGBarcodeScanner.Encode = {
        TEXT_TYPE:     "TEXT_TYPE",
        EMAIL_TYPE:    "EMAIL_TYPE",
        PHONE_TYPE:    "PHONE_TYPE",
        SMS_TYPE:      "SMS_TYPE",
        CONTACT_TYPE:  "CONTACT_TYPE",
        LOCATION_TYPE: "LOCATION_TYPE"
}

//-------------------------------------------------------------------
PGBarcodeScanner.prototype.scan = function(success, fail, options) {
    function successWrapper(result) {
        result.cancelled = (result.cancelled == 1)
        success.call(null, result)
    }

    if (!fail) { fail = function() {}}

    if (typeof fail != "function")  {
        console.log("PGBarcodeScanner.scan failure: failure parameter not a function")
        return
    }

    if (typeof success != "function") {
        fail("success callback parameter must be a function")
        return
    }
  
    if ( null == options ) 
      options = []

    cordova.exec(successWrapper, fail, "PGBarcodeScanner", "scan", options);
}

//-------------------------------------------------------------------
PGBarcodeScanner.prototype.encode = function(type, data, success, fail, options) {
    if (!fail) { fail = function() {}}

    if (typeof fail != "function")  {
        console.log("PGBarcodeScanner.scan failure: failure parameter not a function")
        return
    }

    if (typeof success != "function") {
        fail("success callback parameter must be a function")
        return
    }

    cordova.exec(success, fail, "PGBarcodeScanner", "encode", [{type: type, data: data, options: options}]);
}

//-------------------------------------------------------------------
/*PhoneGap.addConstructor(function() {
    if (!window.plugins) window.plugins = {}

    if (!window.plugins.PGBarcodeScanner) {
        window.plugins.PGBarcodeScanner = new PGBarcodeScanner()
    }
    else {
        console.log("Not installing PGBarcodeScanner: window.plugins.PGBarcodeScanner already exists")
    }
})*/
})();