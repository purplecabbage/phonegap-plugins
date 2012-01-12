//
//  GoogleAnalyticsPlugin.m
//  Gapalytics
//
//  Created by Jesse MacFadyen on 11-04-21.
//  Copyright 2011 Nitobi. All rights reserved.
//

#import "GoogleAnalyticsPlugin.h"



// Dispatch period in seconds
static const NSInteger kGANDispatchPeriodSec = 10;


@implementation GoogleAnalyticsPlugin

- (void) startTrackerWithAccountID:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	NSString* accountId = [arguments objectAtIndex:0];

	[[GANTracker sharedTracker] startTrackerWithAccountID:accountId
										   dispatchPeriod:kGANDispatchPeriodSec
												 delegate:self];
	
}

- (void) trackEvent:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	
	NSString* category = [options valueForKey:@"category"];
	NSString* action = [options valueForKey:@"action"];
	NSString* label = [options valueForKey:@"label"];
	int value = [[options valueForKey:@"value"] intValue];
	
	NSError *error;
	
	if (![[GANTracker sharedTracker] trackEvent:category
										 action:action
										  label:label
										  value:value
									  withError:&error]) {
		// Handle error here
		NSLog(@"GoogleAnalyticsPlugin.trackEvent Error::",[error localizedDescription]);
	}
	
	
	NSLog(@"GoogleAnalyticsPlugin.trackEvent::%@, %@, %@, %d",category,action,label,value);
	
}

- (void) trackPageview:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	NSString* pageUri = [arguments objectAtIndex:0];
	NSError *error;
	if (![[GANTracker sharedTracker] trackPageview:pageUri
										 withError:&error]) {
		// TODO: Handle error here
	}
}



- (void)trackerDispatchDidComplete:(GANTracker *)tracker
                  eventsDispatched:(NSUInteger)eventsDispatched
              eventsFailedDispatch:(NSUInteger)eventsFailedDispatch
{
	NSString* callback = [NSString stringWithFormat:@"window.plugins.googleAnalyticsPlugin.trackerDispatchDidComplete(%d);",
							eventsDispatched];
	[ self.webView stringByEvaluatingJavaScriptFromString:callback];
	
}

- (void) dealloc
{
	[[GANTracker sharedTracker] stopTracker];
	[ super dealloc ];
}

@end
