//
//  SAiOSAdPlugin.m
//  Ad Plugin for PhoneGap
//
//  Created by shazron on 10-07-12.
//  Copyright 2010 Shazron Abdullah. All rights reserved.
//

#import "SAiOSAdPlugin.h"

@interface SAiOSAdPlugin(PrivateMethods)

- (void) __prepare:(BOOL)atBottom;
- (void) __showAd:(BOOL)show;

@end


@implementation SAiOSAdPlugin

@synthesize adView;
@synthesize bannerIsVisible, bannerIsInitialized, bannerIsAtBottom;

#pragma mark -
#pragma mark Public Methods

- (void) prepare:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	NSUInteger argc = [arguments count];
	if (argc > 1) {
		return;
	}
	
	NSString* atBottomValue = [arguments objectAtIndex:0];
	[self __prepare:[atBottomValue boolValue]];
}

- (void) showAd:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	NSUInteger argc = [arguments count];
	if (argc > 1) {
		return;
	}
	
	NSString* showValue = [arguments objectAtIndex:0];
	[self __showAd:[showValue boolValue]];
}

#pragma mark -
#pragma mark Private Methods

- (void) __prepare:(BOOL)atBottom
{
	NSLog(@"SAiOSAdPlugin Prepare Ad At Bottom: %d", atBottom);
	
	Class adBannerViewClass = NSClassFromString(@"ADBannerView");
	if (adBannerViewClass && !self.adView)
	{
		self.adView = [[ADBannerView alloc] initWithFrame:CGRectZero];
		self.adView.requiredContentSizeIdentifiers = [NSSet setWithObjects: ADBannerContentSizeIdentifier320x50, ADBannerContentSizeIdentifier480x32, nil];		
		self.adView.delegate = self;
		
		CGFloat statusBarHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
		if (atBottom) {
			CGRect adViewFrame = self.adView.frame;
			adViewFrame.origin.y = [UIScreen mainScreen].bounds.size.height - statusBarHeight - adViewFrame.size.height;
			self.adView.frame = adViewFrame;
			
			self.bannerIsAtBottom = YES;
		}
		
		self.bannerIsVisible = NO;
		self.bannerIsInitialized = YES;
	}
}

- (void) __showAd:(BOOL)show
{
	NSLog(@"SAiOSAdPlugin Show Ad: %d", show);
	
	if (!self.bannerIsInitialized){
		[self __prepare:NO];
	}
	
	if (!(NSClassFromString(@"ADBannerView") && self.adView)) { // ad classes not available
		return;
	}
	
	if (show == self.bannerIsVisible) { // same state, nothing to do
		return;
	}
	
	CGRect adViewFrame = self.adView.frame;
	CGRect webViewFrame = [super webView].frame;
	CGFloat statusBarHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
	
	if (show)
	{
		if (self.bannerIsAtBottom)
		{
			webViewFrame.size.height -= (adViewFrame.size.height + statusBarHeight);
		}
		else
		{
			webViewFrame.origin.y += adViewFrame.size.height;
			webViewFrame.size.height -= (adViewFrame.size.height + statusBarHeight);
		}

		[UIView beginAnimations:@"blah" context:NULL];
		[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];

		[super webView].frame = webViewFrame;
		[[[super webView] superview] addSubview:self.adView];
		
		[UIView commitAnimations];

		self.bannerIsVisible = YES;
	}
	else 
	{
		if (self.bannerIsAtBottom)
		{
			webViewFrame.size.height += (adViewFrame.size.height + statusBarHeight);
		}
		else
		{
			webViewFrame.origin.y -= adViewFrame.size.height;
			webViewFrame.size.height += (adViewFrame.size.height + statusBarHeight);
		}
		
		[UIView beginAnimations:@"blah" context:NULL];
		[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
		
		[super webView].frame = webViewFrame;
		[self.adView removeFromSuperview];
		
		[UIView commitAnimations];
		
		self.bannerIsVisible = NO;
	}
	
}

#pragma mark -
#pragma ADBannerViewDelegate

- (void)bannerViewDidLoadAd:(ADBannerView *)banner
{
	Class adBannerViewClass = NSClassFromString(@"ADBannerView");
    if (adBannerViewClass)
    {
		NSString* jsString = 
				@"var e = document.createEvent('Events');"
				"e.initEvent('iAdBannerViewDidLoadAdEvent');"
				"document.dispatchEvent(e);";
		[super writeJavascript:jsString];
    }
}

- (void)bannerView:(ADBannerView *)banner didFailToReceiveAdWithError:(NSError*)error
{
	Class adBannerViewClass = NSClassFromString(@"ADBannerView");
    if (adBannerViewClass)
    {
		NSString* jsString = 
		@"var e = document.createEvent('Events');"
		"e.initEvent('iAdBannerViewDidFailToReceiveAdWithErrorEvent');"
		"e.error = '%@';"
		"document.dispatchEvent(e);";
		
		[super writeJavascript:[NSString stringWithFormat:jsString, [error description]]];
    }
}

@end
