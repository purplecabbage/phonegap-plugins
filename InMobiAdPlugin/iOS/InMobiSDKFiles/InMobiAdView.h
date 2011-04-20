/*
 * InMobiAdView.h
 * This class is used to request and display InMobi ads.
 * @author InMobi
 * Copyright 2011 InMobi Technologies Pvt. Ltd.. All rights reserved.
 */

#include <UIKit/UIKit.h>

@protocol InMobiAdDelegate;

@interface InMobiAdView : UIView

/**
 * Supported iOS Banner sizes
 */
// Standard size for an InMobi Ad, 320x48 pixels,designed for all device size
#define INMOBI_AD_UNIT_320x48     9

// Medium Rectangle size for an InMobi Ad, 300x250 pixels, designed for all device size,
// especially in a UISplitView's left pane. 
// can be called from an iPhone/iPod Touch
#define INMOBI_AD_UNIT_300x250    10

// Leaderboard size for an InMobi Ad, 728x90 pixels, designed for iPad's screen size.
#define INMOBI_AD_UNIT_728x90     11

// Full Banner size for an InMobi Ad, 468x60 pixels, designed for iPad's screen size,
// especially in a UIPopoverController or in UIModalPresentationFormSheet placement.
#define INMOBI_AD_UNIT_468x60     12

//Skyscraper size for an InMobi Ad, 120x600 pixels, designed for iPad's screen size.
#define INMOBI_AD_UNIT_120x600    13

/**
 * Initiates an ad request and returns a view that will contain it.  For an
 * iPhone or iPod Touch only the INMOBI_AD_UNIT_320x48 & INMOBI_AD_UNIT_300x250 size will return an ad.
 *
 * The delegate is alerted when the ad is ready to display (or has failed to
 * load); this is a good opportunity to attach the view to your hierarchy.
 * If you already have an ad loaded, and simply want to show
 * a new ad in the same location, you may use -loadNewAd instead (see below).
 *
 * This method should only be called from a run loop in default run loop mode.
 * If you don't know what that means, you're probably ok.
 * If in doubt, check
 * whether ([[NSRunLoop currentRunLoop] currentMode] == NSDefaultRunLoopMode).
 *
 * Eg. [InMobiAdView requestAdUnit:INMOBI_AD_UNIT_728x90 withDelegate:delegate];
 * @return Instance of InMobiAdView, with the dimensions of requested ad-unit.Must not be NULL.
 * @warning Returns NULL if delegate is NULL, also returns NULL if delegate does not conform to @protocol(InMobiAdDelegate)
 */
+ (InMobiAdView *)requestAdUnit:(int)adUnit  withDelegate:(id<InMobiAdDelegate>)delegate;

/**
 * Returns the version of the current inMobi SDK.
 */
+ (NSString *)inMobiSDKVersion;

/**
 * Reload an existing InMobiAdView with a new ad.
 * If an ad not loaded successfully, this call fails silently and the old ad
 * remains onscreen.
 * This method should only be called from a run loop in default run loop mode.
 * To preserve the user experience, we recommend loading new ads no more
 * frequently than once per minute.
 */
- (void)loadNewAd;

/**
 * Setter for InMobiAdView's delegate.
 * Set the delegate to nil, if you do not want to receive notifications from
 */
- (void)setDelegate:(id<InMobiAdDelegate>) delegate;
/**
 * Set adUnit for InMobiAdView. See supported banner sizes as mentioned above.
 */
- (void)setAdUnit:(int)adUnit;

/**
 * The parameter ref-tag  is an optional field that allows the App developer to
 * track Ad performance on specific Ad slots,or positions on the App.
 */
#define INMOBI_REF_TAG @"ref-tag"

/**
 * Optional: Use this method to assign a custom reference tag at the time of making an Ad Request to the InMobi Ad Server.
 */

- (void)setRequestParameter:(NSString *)param forKey:(NSString *)key;

/**
 * This is the designated InMobi internal tag
 * for future purpose only
 */

#define INMOBI_INTERNAL_TAG @"ref-__in__rt"

#pragma mark DEPRECATED_METHODS
/**
 * Equivalent to [InMobiAdView requestAdOfSize:INMOBI_AD_UNIT_320x48 withDelegate:delegate];
 * @deprecated This method is deprecated in version 1.3.0 & above.
 * Use requestAdUnit:withDelegate: instead.
 */

+ (InMobiAdView *)startInmobiAdEngineAttachedDelegate:(id<InMobiAdDelegate>)delegate;

@end
