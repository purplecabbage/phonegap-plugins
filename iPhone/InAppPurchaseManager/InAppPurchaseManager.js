/** 
 * A plugin to enable iOS In-App Purchases.
 *
 * Copyright (c) Matt Kane 2011
 */

var InAppPurchaseManager = function() { 
	PhoneGap.exec('InAppPurchaseManager.setup');
}

/**
 * Makes an in-app purchase. 
 * 
 * @param {String} productId The product identifier. e.g. "com.example.MyApp.myproduct"
 * @param {int} quantity 
 */

InAppPurchaseManager.prototype.makePurchase = function(productId, quantity) {
	var q = parseInt(quantity);
	if(!q) {
		q = 1;
	}
    return PhoneGap.exec('InAppPurchaseManager.makePurchase', productId, q);		
}

/**
 * Asks the payment queue to restore previously completed purchases.
 * The restored transactions are passed to the onRestored callback, so make sure you define a handler for that first.
 * 
 */

InAppPurchaseManager.prototype.restoreCompletedTransactions = function() {
    return PhoneGap.exec('InAppPurchaseManager.restoreCompletedTransactions');		
}


/**
 * Retrieves the localised product data, including price (as a localised string), name, description.
 * You must call this before attempting to make a purchase.
 *
 * @param {String} productId The product identifier. e.g. "com.example.MyApp.myproduct"
 * @param {Function} successCallback Called once for each returned product id. Signature is function(productId, title, description, price)
 * @param {Function} failCallback Called once for each invalid product id. Signature is function(productId)
 */

InAppPurchaseManager.prototype.requestProductData = function(productId, successCallback, failCallback) {
	var key = 'f' + this.callbackIdx++;
	window.plugins.inAppPurchaseManager.callbackMap[key] = {
		success: function(productId, title, description, price ) {
			if (productId == '__DONE') {
				delete window.plugins.inAppPurchaseManager.callbackMap[key]
				return;
			}
			successCallback(productId, title, description, price);
		},
		fail: failCallback
	}
	var callback = 'window.plugins.inAppPurchaseManager.callbackMap.' + key;
    PhoneGap.exec('InAppPurchaseManager.requestProductData', productId, callback + '.success', callback + '.fail');	
}

/* function(transactionIdentifier, productId, transactionReceipt) */
InAppPurchaseManager.prototype.onPurchased = null;

/* function(originalTransactionIdentifier, productId, originalTransactionReceipt) */
InAppPurchaseManager.prototype.onRestored = null;

/* function(errorCode, errorText) */
InAppPurchaseManager.prototype.onFailed = null;


/* This is called from native.*/

InAppPurchaseManager.prototype.updatedTransactionCallback = function(state, errorCode, errorText, transactionIdentifier, productId, transactionReceipt) {
	switch(state) {
		case "PaymentTransactionStatePurchased":
			if(this.onPurchased) {
				this.onPurchased(transactionIdentifier, productId, transactionReceipt);
			} else {
				this.eventQueue.push(arguments);
				this.watchQueue();
			}
			return; 
			
		case "PaymentTransactionStateFailed":
			if(this.onFailed) {
				this.onFailed(errorCode, errorText);
			} else {
				this.eventQueue.push(arguments);
				this.watchQueue();
			}
			return;
		
		case "PaymentTransactionStateRestored":
			if(this.onRestored) {
				this.onRestored(transactionIdentifier, productId, transactionReceipt);
			} else {
				this.eventQueue.push(arguments);
				this.watchQueue();
			}
			return;
	}
}

/*
 * This queue stuff is here because we may be sent events before listeners have been registered. This is because if we have 
 * incomplete transactions when we quit, the app will try to run these when we resume. If we don't register to receive these
 * right away then they may be missed. As soon as a callback has been registered then it will be sent any events waiting
 * in the queue.
 */

InAppPurchaseManager.prototype.runQueue = function() {
	if(!this.eventQueue.length || (!this.onPurchased && !this.onFailed && !this.onRestored)) {
		return;
	}
	var args;
	/* We can't work directly on the queue, because we're pushing new elements onto it */
	var queue = this.eventQueue.slice();
	this.eventQueue = [];
	while(args = queue.shift()) {
		this.updatedTransactionCallback.apply(this, args);
	}
	if(!this.eventQueue.length) {	
		this.unWatchQueue();
	}
}

InAppPurchaseManager.prototype.watchQueue = function() {
	if(this.timer) {
		return;
	}
	this.timer = setInterval("window.plugins.inAppPurchaseManager.runQueue()", 10000);
}

InAppPurchaseManager.prototype.unWatchQueue = function() {
	if(this.timer) {
		clearInterval(this.timer);
		this.timer = null;
	}
}


InAppPurchaseManager.prototype.callbackMap = {};
InAppPurchaseManager.prototype.callbackIdx = 0;
InAppPurchaseManager.prototype.eventQueue = [];
InAppPurchaseManager.prototype.timer = null;

PhoneGap.addConstructor(function()  {
	if(!window.plugins) {
		window.plugins = {};
	}
	window.plugins.inAppPurchaseManager = InAppPurchaseManager.manager = new InAppPurchaseManager();
});
