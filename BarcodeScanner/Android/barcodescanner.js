/**
 * Phonegap Barcode Scanner plugin
 * Copyright (c) Matt Kane 2010
 *
 */
var BarcodeScanner = function() { 

}

BarcodeScanner.Type = {
	QR_CODE: "QR_CODE",
	DATA_MATRIX: "DATA_MATRIX",
	UPC_E: "UPC_E",
	UPC_A: "UPC_A",
	EAN_8: "EAN_8",
	EAN_13: "EAN_13",
	CODE_128: "CODE_128",
	CODE_39: "CODE_39",
	CODE_93: "CODE_93",
	CODABAR: "CODABAR",
	ITF: "ITF",
	RSS14: "RSS14",
	PDF417: "PDF417",
	RSS_EXPANDED: "RSS_EXPANDED",
	PRODUCT_CODE_TYPES: "UPC_A,UPC_E,EAN_8,EAN_13",
	ONE_D_CODE_TYPES: "UPC_A,UPC_E,EAN_8,EAN_13,CODE_39,CODE_93,CODE_128",
	QR_CODE_TYPES: "QR_CODE",
	ALL_CODE_TYPES: null
}

BarcodeScanner.Encode = {
		TEXT_TYPE: "TEXT_TYPE",
		EMAIL_TYPE: "EMAIL_TYPE",
		PHONE_TYPE: "PHONE_TYPE",
		SMS_TYPE: "SMS_TYPE",
		//  CONTACT_TYPE: "CONTACT_TYPE",  // TODO:  not implemented, requires passing a Bundle class from Javascriopt to Java
		//  LOCATION_TYPE: "LOCATION_TYPE" // TODO:  not implemented, requires passing a Bundle class from Javascriopt to Java
	}

BarcodeScanner.prototype.scan = function(types, success, fail, options) {
	
	/* These are the strings used in the dialog that appears if ZXing isn't installed */
	var installTitle = "Install Barcode Scanner?";
	var installMessage = "This requires the free Barcode Scanner app. Would you like to install it now?";
	var yesString = "Yes";
	var noString = "No";
	if (typeof options != 'undefined') {
		if(typeof options.installTitle != 'undefined') {
			installTitle = options.installTitle;
		}

		if(typeof options.installMessage != 'undefined') {
			installMessage = options.installMessage;
		}

		if(typeof options.yesString != 'undefined') {
			yesString = options.yesString;
		}

		if(typeof options.noString != 'undefined') {
			noString = options.noString;
		}
	}
	
	
    return PhoneGap.exec(function(args) {
        success(args);
    }, function(args) {
        fail(args);
    }, 'BarcodeScanner', 'scan', [types, installTitle, installMessage, yesString, noString]);
};

BarcodeScanner.prototype.encode = function(type, data, success, fail, options) {

	/* These are the strings used in the dialog that appears if ZXing isn't installed */
	var installTitle = "Install Barcode Scanner?";
	var installMessage = "This requires the free Barcode Scanner app. Would you like to install it now?";
	var yesString = "Yes";
	var noString = "No";
	if (typeof options != 'undefined') {
		if(typeof options.installTitle != 'undefined') {
			installTitle = options.installTitle;
		}

		if(typeof options.installMessage != 'undefined') {
			installMessage = options.installMessage;
		}

		if(typeof options.yesString != 'undefined') {
			yesString = options.yesString;
		}

		if(typeof options.noString != 'undefined') {
			noString = options.noString;
		}
	}

    return PhoneGap.exec(function(args) {
        success(args);
    }, function(args) {
        fail(args);
    }, 'BarcodeScanner', 'encode', [type, data, installTitle, installMessage, yesString, noString]);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('barcodeScanner', new BarcodeScanner());
	PluginManager.addService("BarcodeScanner","com.beetight.barcodescanner.BarcodeScanner");
});