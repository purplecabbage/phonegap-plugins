//
//  InMobiAdPlugin.m
//  InMobiAdTester
//
//  Created by Jesse MacFadyen on 11-04-14.
//  Copyright 2011 Nitobi. All rights reserved.
//

#import "InMobiAdPlugin.h"


@implementation InMobiAdPlugin

@synthesize _siteId;
@synthesize _atBottom;
@synthesize inmobiAdView;
@synthesize metaDict;

- (void) init:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	// todo, set _siteId from args
	NSString* siteID = [arguments objectAtIndex:0];
	if(siteID)
	{
		self._siteId = siteID;
	}
	self.metaDict = [ options valueForKey:@"meta"];
	self._atBottom = [[options valueForKey:@"atBottom"] boolValue];
}

- (void) showAd:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	if(self.inmobiAdView)
	{
		if(self.inmobiAdView.superview)
		{
			[self.inmobiAdView removeFromSuperview];
		
			CGRect webViewFrame = webView.frame;		
			webViewFrame.size.height = webViewFrame.size.height + 48;
			webViewFrame.origin.y = 0;
			self.webView.frame = webViewFrame;
		}
		
		[self.inmobiAdView setDelegate:nil];
		
		//[self.inmobiAdView loadNewAd];
	}
	self.inmobiAdView = [InMobiAdView requestAdUnit:INMOBI_AD_UNIT_320x48 withDelegate:self];
}

#pragma mark InMobiAdDelegate required methods for an ad request.

/** 
 * @return use this to provide your site id.
 * Get a siteId by registering at http://www.inmobi.com
 * @warning InMobiAdView will return <i>null<i/> if a <i>null<i/> value is returned.
 */
- (NSString *)siteId
{
	if(self._siteId)
	{
		return self._siteId;
	}

	NSLog(@"InMobiAdPlugin Error :: siteId is unknown",0);
	return nil;
}

/**
 * @return the root view controller (InMobiAdView should be part of its view hierarchy).
 * Make sure to return the root view controller.
 * Eg- If your UIViewController( containing InMobiAdView) is a part of UINavigationController's view hierarchy,
 * then return self.navigationController.
 * Else return UIViewController which contains InMobiAdView.
 * @warning InMobiAdView will return <i>null<i/> if a <i>null<i/> value is returned.
 */
- (UIViewController *)rootViewControllerForAd
{
	return [self appViewController];
}

#pragma mark InMobiAdDelegate optional methods for an ad request.
/**
 * Indicates if you wish to provid geo-location information to InMobi.
 * Defaults to NO.
 * see currentLocation
 */
- (BOOL)isLocationInquiryAllowed
{
	BOOL bAllowLoc = [[self.metaDict valueForKey:@"isLocationInquiryAllowed"] boolValue];
	
	return bAllowLoc;
}

/**
 * Provide geo-location if already available.
 *
 * If you have location information available for the user at the time that you
 * make the ad request, you may provide it here.
 * If this method is not implemented and isLocationInquiryAllowed returns true, then Inmobi
 * will request location information directly from the device.
 * see isLocationInquiryAllowed
 */
// - (CLLocation *)currentLocation;

/**
 * Indicates test v/s production mode.
 * Defaults to NO indicating production mode.
 */
- (BOOL)testMode
{
	BOOL bTestMode = [[self.metaDict valueForKey:@"testMode"] boolValue];
	return bTestMode;
}

/* The following functions, if implemented, provide extra information
 * for the ad request. 
 */

/**
 * @return the postal code information about the user.If unknown, return nil.
 */
 - (NSString *)postalCode
{
	NSString* dictPOCode = [self.metaDict valueForKey:@"postalCode"];
	if((id)dictPOCode != [NSNull null])
	{
		return dictPOCode;
	}
	return nil;	
}

/**
 * @return the area code information about the user.If unknown, return nil.
 */
 - (NSString *)areaCode
{
	NSString* dictAreaCode = [self.metaDict valueForKey:@"areaCode"];
	if((id)dictAreaCode != [NSNull null])
	{
		return dictAreaCode;
	}
	return nil;
}

/**
 * @return DOB of the user.If unknown, return nil.
 */
 - (NSDate *)dateOfBirth
{
	return nil;//[ NSDate date];
}

/**
 * @return gender information about the user.If unknown, return G_None.
 */
 - (Gender)gender
{
	id val = [self.metaDict valueForKey:@"gender"];
	if(val != [NSNull null])
	{
		NSUInteger dictGender = [val intValue];
		
		if(dictGender)
		{
			return dictGender;
		}
	}
	return G_None;
}

/**
 * @return NSString containing keywords to help refine targeting 
 * of ads to InMobi.If unknown, return nil.
 * Eg- @"apps,music,podcasts,sms,call"
 */
 - (NSString *)keywords
{
	NSString* dictKeyWords = [self.metaDict valueForKey:@"keywords"];
	if((id)dictKeyWords != [NSNull null])
	{
		return dictKeyWords;
	}
	return nil;	
}

/**
 @return NSString .If unknown, return nil.
 */
 - (NSString *)searchString
{
	NSString* dictSearchString = [self.metaDict valueForKey:@"searchString"];
	if((id)dictSearchString != [NSNull null])
	{
		return dictSearchString;
	}
	return nil;	
}

/** 
 * @return income in Local Currency.
 * if unknown return -1 .
 */
 - (NSUInteger)income
{
	id val = [self.metaDict valueForKey:@"income"];
	if(val != [NSNull null])
	{
		NSUInteger dictIncome = [val intValue];
		if(dictIncome)
		{
			return dictIncome;
		}
	}
	return -1;	
}

/**
 * @return EducationType
 * If unknown return Edu_None.
 */
 - (Education)education
{
	id val = [self.metaDict valueForKey:@"education"];
	if(val != [NSNull null])
	{
		NSUInteger dictEdu = [val intValue];
		
		if(dictEdu)
		{
			return dictEdu;
		}
	}
	return Edu_None;
}

/**
 * @return Ethnicity.
 * If unknown return Eth_None.
 */
 - (Ethnicity)ethnicity
{
	id val = [self.metaDict valueForKey:@"ethnicity"];
	if(val != [NSNull null])
	{
		NSUInteger dictEth = [val intValue];
		
		if(dictEth)
		{
			return dictEth;
		}
	}
	return Eth_None;
}

/**
 * @return age of the targeted user.
 * If unknown, return -1.
 */
 - (NSUInteger)age
{
	id val = [self.metaDict valueForKey:@"age"];
	if(val != [NSNull null])
	{
		NSUInteger dictAge = [val intValue];
		
		if(dictAge)
		{
			return dictAge;
		}
	}
	return -1;	
}

/**
 * @return NSString containing keywords about user's interests,typically to help refine targeting 
 * of ads to InMobi.If unknown, return nil.
 * Eg- @"sports,movies,clothes"
 */
 - (NSString *)interests
{
	NSString* dictInterests = [self.metaDict valueForKey:@"interests"];
	if((id)dictInterests != [NSNull null])
	{
		return dictInterests;
	}
	return nil;
}

#pragma mark InMobiAdDelegate optional notification methods

/**
 * Invoked when an ad request loaded an ad.
 * @param adView The InMobiAdView for which the ad was loaded.
 */
 - (void)adReceivedNotification:(InMobiAdView*)adView
{
	NSLog(@"InMobiAdPlugin adReceivedNotification",0);
	
	if (![inmobiAdView superview]) 
	{
		//check so that adview is not added to superview multiple times
		
		[self.webView.superview addSubview:inmobiAdView];
		
		//CGRect bannerViewFrame = inmobiAdView.frame;
		CGRect webViewFrame = webView.frame;
		
		webViewFrame.size.height = webViewFrame.size.height - 48;
		webViewFrame.origin.y = 48;

		self.webView.frame = webViewFrame;
	}
}

/**
 * Invoked when an ad request failed to load an ad.
 * @param adView The InMobiAdView for which the ad failed to load.
 */
 - (void)adFailedNotification:(InMobiAdView*)adView
{
	NSLog(@"InMobiAdPlugin adFailedNotification",0);
}

/**
 * Invoked just before presenting the user an adModal screen.
 * Embedded webview is an example of a modal screen. This happens in response to clicking on an ad.
 * Use this opportunity to stop animations, time sensitive interactions, etc. in your app
 * see adModalScreenDismissNotification
 * @param adView The InMobiAdView which was clicked.
 */
 - (void)adModalScreenDisplayNotification:(InMobiAdView*)adView
{
	NSLog(@"InMobiAdPlugin adModalScreenDisplayNotification",0);	
}

/**
 * Invoked after dismissing the user an adModal screen.
 * Use this opportunity to resume anything that was suspended just before the modal was shown
 *
 * see adModalScreenDisplayNotification
 * @param adView The InMobiAdView which was clicked. 
 */
 - (void)adModalScreenDismissNotification:(InMobiAdView*)adView
{
	NSLog(@"InMobiAdPlugin adModalScreenDismissNotification",0);	
}

/**
 * Send just before the application will close because the user clicked on an ad.
 * Clicking on any ad will either call this or adModalScreenDisplayNotification.
 * The normal UIApplication applicationWillTerminate: delegate method will be called
 * after this.
 * @param adView The InMobiAdView which was clicked.
 */
 - (void)applicationWillTerminateFromAd:(InMobiAdView *)adView
{
	NSLog(@"InMobiAdPlugin applicationWillTerminateFromAd",0);	
}

/**
 * Invoked before and after the keyboard is displayed or it is removed from the screen.
 */
 - (void)keyboardNotification:(BOOL)displayed attachedInMobiAdView:(InMobiAdView*)adView
{
	NSLog(@"InMobiAdPlugin keyboardNotification",0);	
}

- (void) dealloc
{
	[inmobiAdView setDelegate:nil];
	
	[super dealloc];
}


@end
