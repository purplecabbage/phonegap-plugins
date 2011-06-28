//
//  SAiOSAdPlugin.h
//  ADPlugin for PhoneGap
//
//  Created by shazron on 10-07-12.
//  Copyright 2010 Shazron Abdullah. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <iAd/iAd.h>

#ifdef PHONEGAP_FRAMEWORK
    #import <PhoneGap/PGPlugin.h>
#else
    #import "PGPlugin.h"
#endif


@interface SAiOSAdPlugin : PGPlugin <ADBannerViewDelegate> {
	
	ADBannerView* adView;
	
	BOOL bannerIsVisible;
	BOOL bannerIsInitialized;
	BOOL bannerIsAtBottom;
}

@property (nonatomic, retain)	ADBannerView* adView;
@property (assign)				BOOL bannerIsVisible;
@property (assign)				BOOL bannerIsInitialized;
@property (assign)				BOOL bannerIsAtBottom;
@property (assign)				BOOL isLandscape;


- (void) prepare:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) showAd:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) orientationChanged:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
