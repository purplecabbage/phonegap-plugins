/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 * 
 * Copyright (c) 2011, IBM Corporation
 */

var BarcodeScanner = function() {
};

BarcodeScanner.prototype.scan = function(successCallback, errorCallback) {
    PhoneGap.exec(successCallback, errorCallback, 'BarcodeScanner', 'scan', []);
};

BarcodeScanner.prototype.encode = function(data, successCallback, errorCallback) {
    PhoneGap.exec(successCallback, errorCallback, 'BarcodeScanner', 'encode', [data]);
};

PhoneGap.addConstructor(function() {
    PhoneGap.addPlugin('barcodeScanner', new BarcodeScanner());
});