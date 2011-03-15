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

@synthesize bannerView;
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
	if (adBannerViewClass && !self.bannerView)
	{
		bannerView = [[ADBannerView alloc] initWithFrame:CGRectZero];

		bannerView.requiredContentSizeIdentifiers = [NSSet setWithObjects: ADBannerContentSizeIdentifier320x50, ADBannerContentSizeIdentifier480x32, nil];		
		
		bannerView.delegate = self;

		self.bannerIsAtBottom = atBottom;
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
	
	if (!(NSClassFromString(@"ADBannerView") && self.bannerView)) { // ad classes not available
		return;
	}
	
	if (show == self.bannerIsVisible) { // same state, nothing to do
		return;
	}
	
	CGRect bannerViewFrame = bannerView.frame;
	CGRect webViewFrame = webView.frame;
	CGRect screenFrame = [ [ UIScreen mainScreen ] applicationFrame ];
	
	//CGFloat statusBarHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
	
	[UIView beginAnimations:@"blah" context:NULL];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	
	if (show)
	{
		CGFloat bannerHeight = 50.0;
		
		// First, setup the banner's content size and adjustment based on the current orientation
		if(UIInterfaceOrientationIsLandscape(self.appViewController.interfaceOrientation))
		{
			bannerView.currentContentSizeIdentifier = ADBannerContentSizeIdentifierLandscape;
			bannerHeight = 32.0;
		}
		else
		{
			bannerView.currentContentSizeIdentifier = ADBannerContentSizeIdentifierPortrait;
		}
		
		bannerViewFrame.size.height = bannerHeight;
		bannerViewFrame.size.width = screenFrame.size.width;
		
		if (self.bannerIsAtBottom) 
		{
			bannerViewFrame.origin.y = screenFrame.size.height - bannerViewFrame.size.height;
		}
		else // aka: at the top
		{
			webViewFrame.origin.y += bannerViewFrame.size.height;
		}

		webViewFrame.size.height -= bannerViewFrame.size.height;
		webView.frame = webViewFrame;
		
		[ webView.superview addSubview:self.bannerView];
		
		self.bannerView.frame = bannerViewFrame;
		
		bannerView.backgroundColor = [UIColor blackColor];
		
		self.bannerIsVisible = YES;
	}
	else 
	{
		if (self.bannerIsAtBottom) 
		{

		}
		else // aka: at the top
		{
			webViewFrame.origin.y = screenFrame.origin.y;
		}

		
		webViewFrame.size.height += bannerViewFrame.size.height;
		
		webView.frame = webViewFrame;
		[ bannerView removeFromSuperview];
		
		
		self.bannerIsVisible = NO;
	}
	
	[UIView commitAnimations];
	
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration
{
    if (UIInterfaceOrientationIsLandscape(toInterfaceOrientation))
        self.bannerView.currentContentSizeIdentifier =
		ADBannerContentSizeIdentifierLandscape;
    else
        self.bannerView.currentContentSizeIdentifier =
		ADBannerContentSizeIdentifierPortrait;
}

#pragma mark -
#pragma ADBannerViewDelegate

- (void)bannerViewDidLoadAd:(ADBannerView *)banner
{
	Class adBannerViewClass = NSClassFromString(@"ADBannerView");
    if (adBannerViewClass)
    {
		NSString* jsString = 
		@"(function(){"
		"var e = document.createEvent('Events');"
		"e.initEvent('iAdBannerViewDidLoadAdEvent');"
		"document.dispatchEvent(e);"
		"})();";

		[super writeJavascript:jsString];
    }
}

- (void)bannerView:(ADBannerView *)banner didFailToReceiveAdWithError:(NSError*)error
{
	Class adBannerViewClass = NSClassFromString(@"ADBannerView");
    if (adBannerViewClass)
    {
		NSString* jsString = 
		@"(function(){"
		"var e = document.createEvent('Events');"
		"e.initEvent('iAdBannerViewDidFailToReceiveAdWithErrorEvent');"
		"e.error = '%@';"
		"document.dispatchEvent(e);"
		"})();";
		
		[super writeJavascript:[NSString stringWithFormat:jsString, [error description]]];
    }
}

- (void)bannerViewActionDidFinish:(ADBannerView *)banner
{
	
}

- (BOOL)bannerViewActionShouldBegin:(ADBannerView *)banner willLeaveApplication:(BOOL)willLeave
{
    NSLog(@"Banner view is beginning an ad action");
    BOOL shouldExecuteAction = YES;//[self allowActionToRun]; // your application implements this method
    if (!willLeave && shouldExecuteAction)
    {
        // insert code here to suspend any services that might conflict with the advertisement
    }
    return shouldExecuteAction;
}

@end
