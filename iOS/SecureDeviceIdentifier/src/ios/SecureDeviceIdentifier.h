//
//  SecureDeviceIdentifier.h
//
// Created by Olivier Louvignes on 2012-05-31.
//
// Copyright 2012 Olivier Louvignes. All rights reserved.
// MIT Licensed

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

@interface SecureDeviceIdentifier : CDVPlugin {
}

#pragma mark - Properties

@property (nonatomic, copy) NSString* callbackId;
@property (nonatomic, copy) NSString* secureUDID;

#pragma mark - Instance methods

- (void)get:(CDVInvokedUrlCommand*)command;

@end

#pragma mark - Logging tools

#ifdef DEBUG
#   define DLog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);
#else
#   define DLog(...)
#endif
#define ALog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);
