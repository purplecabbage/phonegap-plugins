//
//  Notification.h
//  PhoneGap
//
//  Created by Michael Nachbaur on 16/04/09.
//  Copyright 2009 Decaf Ninja Software. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioServices.h>
#ifdef CORDOVA_FRAMEWORK
	#import <Cordova/CDVPlugin.h>
#else
	#import "CDVPlugin.h"
#endif
#import "LoadingView.h"

@interface NotificationEx : CDVPlugin {
	NExLoadingView* loadingView;
}

@property (nonatomic, retain) NExLoadingView* loadingView;

- (void)loadingStart:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void)loadingStop:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

- (void)activityStart:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void)activityStop:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
