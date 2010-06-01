//

// 
//
//  Created by Jesse MacFadyen on 10-05-29.
//  Copyright 2010 Nitobi. All rights reserved.
//

#import "ChildBrowserCommand.h"
#import "ChildBrowserViewController.h"
#import "PhoneGapViewController.h"


@implementation ChildBrowserCommand

- (void) showWebPage:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options // args: url
{	
	ChildBrowserViewController* childBrowser = [ [ ChildBrowserViewController alloc ] initWithScale:FALSE ];
	
/* // TODO: Work in progress
	NSString* strOrientations = [ options objectForKey:@"supportedOrientations"];
	NSArray* supportedOrientations = [strOrientations componentsSeparatedByString:@","];
*/
	PhoneGapViewController* cont = (PhoneGapViewController*)[ super appViewController ];
	childBrowser.supportedOrientations = cont.supportedOrientations;
	[ cont presentModalViewController:childBrowser animated:YES ];
	
	NSString *url = (NSString*) [arguments objectAtIndex:0];

	[childBrowser loadURL:url  ];
	[childBrowser release];
}


@end
