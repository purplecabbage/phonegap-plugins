//
//  GoogleAnalyticsPlugin.h
//  Gapalytics
//
//  Created by Jesse MacFadyen on 11-04-21.
//  Copyright 2011 Nitobi. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"
#import "GANTracker.h"


@interface GoogleAnalyticsPlugin : PhoneGapCommand<GANTrackerDelegate> {

}

- (void) startTrackerWithAccountID:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) trackEvent:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) trackPageview:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
