//
//  SAiOSAdPlugin.h
//  ADPlugin for PhoneGap
//
//  Created by shazron on 10-07-12.
//  Copyright 2010 Shazron Abdullah. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"
#import <iAd/iAd.h>

@interface SAiOSAdPlugin : PhoneGapCommand <ADBannerViewDelegate> {
	
	ADBannerView* bannerView;
	
	BOOL bannerIsVisible;
	BOOL bannerIsInitialized;
	BOOL bannerIsAtBottom;
	
	NSString* portraitContentIndentifier;
	NSString* landscapeContentIndentifier;
	
}

@property (nonatomic, retain)	ADBannerView* bannerView;
@property (assign)				BOOL bannerIsVisible;
@property (assign)				BOOL bannerIsInitialized;
@property (assign)				BOOL bannerIsAtBottom;

@property (copy) NSString* portraitContentIndentifier;
@property (copy) NSString* landscapeContentIndentifier;


- (void) prepare:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) showAd:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
