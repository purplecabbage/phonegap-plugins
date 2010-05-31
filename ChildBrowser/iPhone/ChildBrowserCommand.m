//

// 
//
//  Created by Jesse MacFadyen on 10-05-29.
//  Copyright 2010 Nitobi. All rights reserved.
//

#import "ChildBrowserCommand.h"
#import "ChildBrowserViewController.h"


@implementation ChildBrowserCommand

- (void) showWebPage:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options // args: url
{	
	ChildBrowserViewController* childBrowser = [ [ ChildBrowserViewController alloc ] initWithScale:FALSE ];
	[ [ super appViewController ] presentModalViewController:childBrowser animated:YES ];
	
	NSString *url = (NSString*) [arguments objectAtIndex:0];
	//stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	[childBrowser loadURL:url  ];
	[childBrowser release];
}


@end
