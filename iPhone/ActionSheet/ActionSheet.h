//
//  ActionSheet.h
//  
// Created by Olivier Louvignes on 11/27/2011.
// Added Cordova 1.5 support - @RandyMcMillan 2012
// Copyright 2011 Olivier Louvignes. All rights reserved.
// MIT Licensed

#import <Foundation/Foundation.h>
#ifdef PHONEGAP_FRAMEWORK
#import <PhoneGap/PGPlugin.h>
#endif
//#else
#ifdef CORDOVA_FRAMEWORK
#import <CORDOVA/CDVPlugin.h>
#endif

#ifdef PHONEGAP_FRAMEWORK
@interface ActionSheet : PGPlugin <UIActionSheetDelegate> {
#endif
#ifdef CORDOVA_FRAMEWORK
@interface ActionSheet : CDVPlugin <UIActionSheetDelegate>  {
#endif
    
    
	NSString* callbackID;

}

@property (nonatomic, copy) NSString* callbackID;

//Instance Method  
- (void) create:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end