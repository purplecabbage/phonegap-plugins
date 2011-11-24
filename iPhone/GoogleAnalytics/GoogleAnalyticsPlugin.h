//
//  GoogleAnalyticsPlugin.h
//  Google Analytics plugin for PhoneGap
//
//  Created by Jesse MacFadyen on 11-04-21.
//  MIT Licensed
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
