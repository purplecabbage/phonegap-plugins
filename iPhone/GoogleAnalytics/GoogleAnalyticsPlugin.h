//
//  GoogleAnalyticsPlugin.h
//  Gapalytics
//
//  Created by Jesse MacFadyen on 11-04-21.
//  Copyright 2011 Nitobi. All rights reserved.
//

import <Foundation/Foundation.h>
#ifdef PHONEGAP_FRAMEWORK
#import <PhoneGap/PGPlugin.h>
#import <PhoneGap/NSData+Base64.h>
#import <PhoneGap/JSONKit.h>
#else
#import "PGPlugin.h"
#import "NSData+Base64.h"
#import "JSONKit.h"
#endif

#import "GANTracker.h"


@interface GoogleAnalyticsPlugin : PGPlugin<GANTrackerDelegate> {

}

- (void) startTrackerWithAccountID:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) trackEvent:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) trackPageview:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
