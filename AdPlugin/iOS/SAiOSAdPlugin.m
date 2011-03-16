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
- (void) __showAd:(BOOL)show atBottom:(BOOL)atBottom;

@end


@implementation SAiOSAdPlugin

@synthesize bannerView;
@synthesize bannerIsVisible;
@synthesize bannerIsInitialized;
@synthesize bannerIsAtBottom;
@synthesize portraitContentIndentifier;
@synthesize landscapeContentIndentifier;


#pragma mark -
#pragma mark Public Methods

- (void) prepare:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
  BOOL atBottom = NO;

  NSUInteger argc = [arguments count];
	if (argc > 1) {
    return;
  } else if (argc == 1) {
    atBottom = [[arguments objectAtIndex:0] boolValue];
  }

  [self __prepare:atBottom];
}

- (void) showAd:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
  BOOL showValue = YES;
  BOOL atBottom  = NO;
  
  NSUInteger argc = [arguments count];
  if (argc > 2) {
  	return;
  } else if (argc > 0) {
    showValue = [[arguments objectAtIndex:0] boolValue];
    if (argc > 1) {
      atBottom = [[arguments objectAtIndex:1] boolValue];
    }
  }
  
  [self __showAd:showValue atBottom:atBottom];
}

#pragma mark -
#pragma mark Private Methods

-(NSString*) getADBannerContentSizeIdentifierPortrait 
{
	if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 4.2)
	{
		return ADBannerContentSizeIdentifierPortrait;
	}
	
	return ADBannerContentSizeIdentifier320x50;
}

-(NSString*) getADBannerContentSizeIdentifierLandscape 
{
	if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 4.2) 
	{
		return ADBannerContentSizeIdentifierLandscape;
	}
	
	return ADBannerContentSizeIdentifier480x32;
}

- (void) __prepare:(BOOL)atBottom
{
	NSLog(@"SAiOSAdPlugin Prepare Ad At Bottom: %d", atBottom);
	
	self.portraitContentIndentifier = [self getADBannerContentSizeIdentifierPortrait];
	self.landscapeContentIndentifier = [self getADBannerContentSizeIdentifierLandscape];	
	
	Class adBannerViewClass = NSClassFromString(@"ADBannerView");
	if (adBannerViewClass && !self.bannerView)
	{
		bannerView = [[ADBannerView alloc] initWithFrame:CGRectZero];
    
		bannerView.requiredContentSizeIdentifiers = [NSSet setWithObjects: self.portraitContentIndentifier, self.landscapeContentIndentifier, nil];		
		
		bannerView.delegate = self;
    
		self.bannerIsAtBottom = atBottom;
		self.bannerIsVisible = NO;
		self.bannerIsInitialized = YES;
	}
}

- (void) __showAd:(BOOL)show atBottom:(BOOL)atBottom
{
	NSLog(@"SAiOSAdPlugin Show Ad: %d atBottom: %d", show, atBottom);

  // same state, nothing to do
	if (show == self.bannerIsVisible) return;

	if (show && !self.bannerIsInitialized) [self __prepare:atBottom];
	
  // ad classes not available
	if (!(NSClassFromString(@"ADBannerView") && self.bannerView)) return;
	
	CGRect bannerViewFrame = bannerView.frame;
	CGRect webViewFrame    = webView.frame;
	CGRect screenFrame     = [[UIScreen mainScreen] applicationFrame];
	
	[UIView beginAnimations:@"blah" context:NULL];
	[UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
	
	if (show && self.bannerView.bannerLoaded)
	{
		CGFloat bannerHeight = 50.0;
		
		// First, setup the banner's content size and adjustment based on the current orientation
		if(UIInterfaceOrientationIsLandscape(self.appViewController.interfaceOrientation))
		{
			bannerView.currentContentSizeIdentifier = self.landscapeContentIndentifier;
			bannerHeight = 32.0;
		}
		else
		{
			bannerView.currentContentSizeIdentifier = self.portraitContentIndentifier;
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
