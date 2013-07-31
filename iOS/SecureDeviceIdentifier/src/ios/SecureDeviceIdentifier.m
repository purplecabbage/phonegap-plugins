//
//  SecureDeviceIdentifier.m
//
// Created by Olivier Louvignes on 2012-05-31.
//
// Copyright 2012 Olivier Louvignes. All rights reserved.
// MIT Licensed

#import "SecureDeviceIdentifier.h"
#import "SecureUDID.h"

@implementation SecureDeviceIdentifier

@synthesize callbackId = _callbackId, secureUDID = _secureUDID;

- (void)get:(CDVInvokedUrlCommand*)command {
	
	self.callbackId = command.callbackId;	

	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{  
		//[self.commandDelegate runInBackground:^{
		NSDictionary *options = [command.arguments objectAtIndex:0];	
		NSString *domain = [options objectForKey:@"domain"] ?: @"";
		NSString *key = [options objectForKey:@"key"] ?: @"";
		self.secureUDID = [SecureUDID UDIDForDomain:domain usingKey:key]; //NSLog(@"self.secureUDID %@", self.secureUDID);

		dispatch_async(dispatch_get_main_queue(), ^(void) {
			// Create Plugin Result
			CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:self.secureUDID];
			// Call  the Success Javascript function
			[self writeJavascript: [pluginResult toSuccessCallbackString:self.callbackId]];
	    });
	});



}

@end
