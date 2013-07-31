/*
 *  This code is adapted from the work of Michael Nachbaur
 *  by Simon Madine of The Angry Robot Zombie Factory
 *   - Converted to Cordova 1.6.1 by Joseph Stuhr.
 *  2012-04-19
 *  MIT licensed
 *
 */

#import "Badge.h"

@implementation Badge

- (void)setBadge:(CDVInvokedUrlCommand*)command {
    
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
		dispatch_async(dispatch_get_main_queue(), ^(void) {
			[[UIApplication sharedApplication] setApplicationIconBadgeNumber:[[ command.arguments objectAtIndex:0] intValue]];
	    });
	});
	
}

@end