/*
 * InMobiAdDelegate.h
 * Defines the InMobiAdDelegate protocol.
 * @author: InMobi
 * Copyright 2011 InMobi Technologies Pvt. Ltd.. All rights reserved.
 */
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>
#import "InMobiEnumTypes.h"

@class InMobiAdView;

@protocol InMobiAdDelegate<NSObject>

@required

#pragma mark InMobiAdDelegate required methods for an ad request.
/** 
 * @return use this to provide your site id.
 * Get a siteId by registering at http://www.inmobi.com
 * @warning InMobiAdView will return <i>null<i/> if a <i>null<i/> value is returned.
 */
- (NSString *)siteId;

/**
 * @return the root view controller (InMobiAdView should be part of its view hierarchy).
 * Make sure to return the root view controller.
 * Eg- If your UIViewController( containing InMobiAdView) is a part of UINavigationController's view hierarchy,
 * then return self.navigationController.
 * Else return UIViewController which contains InMobiAdView.
 * @warning InMobiAdView will return <i>null<i/> if a <i>null<i/> value is returned.
 */
- (UIViewController *)rootViewControllerForAd;

@optional
#pragma mark InMobiAdDelegate optional methods for an ad request.
/**
 * Indicates if you wish to provid geo-location information to InMobi.
 * Defaults to NO.
 * see currentLocation
 */
- (BOOL)isLocationInquiryAllowed;

/**
 * Provide geo-location if already available.
 *
 * If you have location information available for the user at the time that you
 * make the ad request, you may provide it here.
 * If this method is not implemented and isLocationInquiryAllowed returns true, then Inmobi
 * will request location information directly from the device.
 * see isLocationInquiryAllowed
 */
- (CLLocation *)currentLocation;

/**
 * Indicates test v/s production mode.
 * Defaults to NO indicating production mode.
 */
- (BOOL)testMode;

/* The following functions, if implemented, provide extra information
 * for the ad request. 
 */

/**
 * @return the postal code information about the user.If unknown, return nil.
 */
- (NSString *)postalCode; 

/**
 * @return the area code information about the user.If unknown, return nil.
 */
- (NSString *)areaCode; 

/**
 * @return DOB of the user.If unknown, return nil.
 */
- (NSDate *)dateOfBirth;

/**
 * @return gender information about the user.If unknown, return G_None.
 */
- (Gender)gender;

/**
 * @return NSString containing keywords to help refine targeting 
 * of ads to InMobi.If unknown, return nil.
 * Eg- @"apps,music,podcasts,sms,call"
 */
- (NSString *)keywords; 

/**
 @return NSString .If unknown, return nil.
 */
- (NSString *)searchString;

/** 
 * @return income in Local Currency.
 * if unknown return -1 .
 */
- (NSUInteger)income; 

/**
 * @return EducationType
 * If unknown return Edu_None.
 */
- (Education)education;		

/**
 * @return Ethnicity.
 * If unknown return Eth_None.
 */
- (Ethnicity)ethnicity;

/**
 * @return age of the targeted user.
 * If unknown, return -1.
 */
- (NSUInteger)age;

/**
 * @return NSString containing keywords about user's interests,typically to help refine targeting 
 * of ads to InMobi.If unknown, return nil.
 * Eg- @"sports,movies,clothes"
 */
- (NSString *)interests;

@optional
#pragma mark InMobiAdDelegate optional notification methods
/**
 * Invoked when an ad request loaded an ad.
 * @param adView The InMobiAdView for which the ad was loaded.
 */
- (void)adReceivedNotification:(InMobiAdView*)adView;

/**
 * Invoked when an ad request failed to load an ad.
 * @param adView The InMobiAdView for which the ad failed to load.
 */
- (void)adFailedNotification:(InMobiAdView*)adView;

/**
 * Invoked just before presenting the user an adModal screen.
 * Embedded webview is an example of a modal screen. This happens in response to clicking on an ad.
 * Use this opportunity to stop animations, time sensitive interactions, etc. in your app
 * see adModalScreenDismissNotification
 * @param adView The InMobiAdView which was clicked.
 */
- (void)adModalScreenDisplayNotification:(InMobiAdView*)adView;

/**
 * Invoked after dismissing the user an adModal screen.
 * Use this opportunity to resume anything that was suspended just before the modal was shown
 *
 * see adModalScreenDisplayNotification
 * @param adView The InMobiAdView which was clicked. 
 */
- (void)adModalScreenDismissNotification:(InMobiAdView*)adView;

/**
 * Send just before the application will close because the user clicked on an ad.
 * Clicking on any ad will either call this or adModalScreenDisplayNotification.
 * The normal UIApplication applicationWillTerminate: delegate method will be called
 * after this.
 * @param adView The InMobiAdView which was clicked.
 */
- (void)applicationWillTerminateFromAd:(InMobiAdView *)adView;

/**
 * Invoked before and after the keyboard is displayed or it is removed from the screen.
 */
- (void)keyboardNotification:(BOOL)displayed attachedInMobiAdView:(InMobiAdView*)adView;

@end
