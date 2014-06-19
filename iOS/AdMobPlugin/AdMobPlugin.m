#import "AdMobPlugin.h"
#import "GADAdMobExtras.h"
#import "GADAdSize.h"
#import "GADBannerView.h"

@interface AdMobPlugin ()

- (void)createGADBannerViewWithPubId:(NSString *)pubId
                          bannerType:(GADAdSize)adSize;
- (void)requestAdWithTesting:(BOOL)isTesting
                      extras:(NSDictionary *)extraDict;
- (void)resizeViews;
- (GADAdSize)GADAdSizeFromString:(NSString *)string;
- (void)deviceOrientationChange:(NSNotification *)notification;

@end

@implementation AdMobPlugin

@synthesize bannerView = bannerView_;

#pragma mark Cordova JS bridge

- (CDVPlugin *)initWithWebView:(UIWebView *)theWebView {
  self = (AdMobPlugin *)[super initWithWebView:theWebView];
  if (self) {
    // These notifications are required for re-placing the ad on orientation
    // changes. Start listening for notifications here since we need to
    // translate the Smart Banner constants according to the orientation.
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    [[NSNotificationCenter defaultCenter]
        addObserver:self
           selector:@selector(deviceOrientationChange:)
               name:UIDeviceOrientationDidChangeNotification
             object:nil];
  }
  return self;
}

// The javascript from the AdMob plugin calls this when createBannerView is
// invoked. This method parses the arguments passed in.
- (void)createBannerView:(CDVInvokedUrlCommand *)command {
  CDVPluginResult *pluginResult;
  NSString *callbackId = command.callbackId;
  GADAdSize adSize = [self GADAdSizeFromString:
                         [command argumentAtIndex:AD_SIZE_ARG_INDEX]];
  positionAdAtTop_ = NO;
  // We don't need positionAtTop to be set, but we need values for adSize and
  // publisherId if we don't want to fail.
  if (![command argumentAtIndex:PUBLISHER_ID_ARG_INDEX]) {
    // Call the error callback that was passed in through the javascript
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                     messageAsString:@"AdMobPlugin:"
                                                     @"Invalid publisher Id"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    return;
  } else if (GADAdSizeEqualToSize(adSize, kGADAdSizeInvalid)) {
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                     messageAsString:@"AdMobPlugin:"
                                                     @"Invalid ad size"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    return;
  }
  if ([command argumentAtIndex:POSITION_AT_TOP_ARG_INDEX]) {
    positionAdAtTop_=
        [[command argumentAtIndex:POSITION_AT_TOP_ARG_INDEX] boolValue];
  }

  NSString *publisherId = [command argumentAtIndex:PUBLISHER_ID_ARG_INDEX];
  [self createGADBannerViewWithPubId:publisherId
                          bannerType:adSize];

  // Call the success callback that was passed in through the javascript.
  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)requestAd:(CDVInvokedUrlCommand *)command {
  CDVPluginResult *pluginResult;
  NSString *callbackId = command.callbackId;

  if (!self.bannerView) {
    // Try to prevent requestAd from being called without createBannerView first
    // being called.
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                     messageAsString:@"AdMobPlugin:"
                                                     @"No ad view exists"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    return;
  }

  NSDictionary *extrasDictionary = nil;
  if ([command argumentAtIndex:EXTRAS_ARG_INDEX]) {
    extrasDictionary = [NSDictionary dictionaryWithDictionary:
                           [command argumentAtIndex:EXTRAS_ARG_INDEX]];
  }
  BOOL isTesting =
      [[command argumentAtIndex:IS_TESTING_ARG_INDEX] boolValue];
  [self requestAdWithTesting:isTesting
                      extras:extrasDictionary];

  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (GADAdSize)GADAdSizeFromString:(NSString *)string {
  if ([string isEqualToString:@"BANNER"]) {
    return kGADAdSizeBanner;
  } else if ([string isEqualToString:@"IAB_MRECT"]) {
    return kGADAdSizeMediumRectangle;
  } else if ([string isEqualToString:@"IAB_BANNER"]) {
    return kGADAdSizeFullBanner;
  } else if ([string isEqualToString:@"IAB_LEADERBOARD"]) {
    return kGADAdSizeLeaderboard;
  } else if ([string isEqualToString:@"SMART_BANNER"]) {
    // Have to choose the right Smart Banner constant according to orientation.
    UIDeviceOrientation currentOrientation =
        [[UIDevice currentDevice] orientation];
    if (UIInterfaceOrientationIsPortrait(currentOrientation)) {
      return kGADAdSizeSmartBannerPortrait;
    }
    else {
      return kGADAdSizeSmartBannerLandscape;
    }
  } else {
    return kGADAdSizeInvalid;
  }
}

#pragma mark Ad Banner logic

- (void)createGADBannerViewWithPubId:(NSString *)pubId
                          bannerType:(GADAdSize)adSize {
  self.bannerView = [[[GADBannerView alloc] initWithAdSize:adSize] autorelease];
  self.bannerView.adUnitID = pubId;
  self.bannerView.delegate = self;
  self.bannerView.rootViewController = self.viewController;
}

- (void)requestAdWithTesting:(BOOL)isTesting
                      extras:(NSDictionary *)extrasDict {
  GADRequest *request = [GADRequest request];

  if (isTesting) {
    // Make the request for a test ad. Put in an identifier for the simulator as
    // well as any devices you want to receive test ads.
    request.testDevices =
        [NSArray arrayWithObjects:
            GAD_SIMULATOR_ID,
            // TODO: Add your device test identifiers here. They are
            // printed to the console when the app is launched.
            nil];
  }
  if (extrasDict) {
    GADAdMobExtras *extras = [[[GADAdMobExtras alloc] init] autorelease];
    NSMutableDictionary *modifiedExtrasDict =
        [[NSMutableDictionary alloc] initWithDictionary:extrasDict];
    [modifiedExtrasDict removeObjectForKey:@"cordova"];
    [modifiedExtrasDict setValue:@"1" forKey:@"cordova"];
    extras.additionalParameters = modifiedExtrasDict;
    [request registerAdNetworkExtras:extras];
  }
  [self.bannerView loadRequest:request];
  // Add the ad to the main container view, and resize the webview to make space
  // for it.
  [self.webView.superview addSubview:self.bannerView];
  [self resizeViews];
}

- (void)resizeViews {
  // If the banner hasn't been created yet, no need for resizing views.
  if (!self.bannerView) {
    return;
  }

  BOOL adIsShowing =
      [self.webView.superview.subviews containsObject:self.bannerView];
  // If the ad is not showing or the ad is hidden, we don't want to resize
  // anything.
  if (!adIsShowing || self.bannerView.hidden) {
    return;
  }

  UIDeviceOrientation currentOrientation =
      [[UIDevice currentDevice] orientation];
  // Handle changing Smart Banner constants for the user.
  BOOL adIsSmartBannerPortrait =
      GADAdSizeEqualToSize(self.bannerView.adSize,
                           kGADAdSizeSmartBannerPortrait);
  BOOL adIsSmartBannerLandscape =
      GADAdSizeEqualToSize(self.bannerView.adSize,
                           kGADAdSizeSmartBannerLandscape);
  if ((adIsSmartBannerPortrait) &&
      (UIInterfaceOrientationIsLandscape(currentOrientation))) {
    self.bannerView.adSize = kGADAdSizeSmartBannerLandscape;
  } else if ((adIsSmartBannerLandscape) &&
             (UIInterfaceOrientationIsPortrait(currentOrientation))) {
    self.bannerView.adSize = kGADAdSizeSmartBannerPortrait;
  }

  // Frame of the main Cordova webview.
  CGRect webViewFrame = self.webView.frame;
  // Frame of the main container view that holds the Cordova webview.
  CGRect superViewFrame = self.webView.superview.frame;
  CGRect bannerViewFrame = self.bannerView.frame;
  CGRect frame = bannerViewFrame;
  // The updated x and y coordinates for the origin of the banner.
  CGFloat yLocation = 0.0;
  CGFloat xLocation = 0.0;

  if (positionAdAtTop_) {
    // Move the webview underneath the ad banner.
    webViewFrame.origin.y = bannerViewFrame.size.height;
    // Center the banner using the value of the origin.
    if (UIInterfaceOrientationIsLandscape(currentOrientation)) {
      // The superView has not had its width and height updated yet so use those
      // values for the x and y of the new origin respectively.
      xLocation = (superViewFrame.size.height -
                      bannerViewFrame.size.width) / 2.0;
    } else {
      xLocation = (superViewFrame.size.width -
                      bannerViewFrame.size.width) / 2.0;
    }
  } else {
    // Move the webview to the top of the screen.
    webViewFrame.origin.y = 0;
    // Need to center the banner both horizontally and vertically.
    if (UIInterfaceOrientationIsLandscape(currentOrientation)) {
      yLocation = superViewFrame.size.width -
                      bannerViewFrame.size.height;
      xLocation = (superViewFrame.size.height -
                      bannerViewFrame.size.width) / 2.0;
    } else {
      yLocation = superViewFrame.size.height -
                      bannerViewFrame.size.height;
      xLocation = (superViewFrame.size.width -
                      bannerViewFrame.size.width) / 2.0;
    }
  }
  frame.origin = CGPointMake(xLocation, yLocation);
  bannerView_.frame = frame;

  if (UIInterfaceOrientationIsLandscape(currentOrientation)) {
    // The super view's frame hasn't been updated so use its width
    // as the height.
    webViewFrame.size.height = superViewFrame.size.width -
                                   bannerViewFrame.size.height;
  } else {
    webViewFrame.size.height = superViewFrame.size.height -
                                   bannerViewFrame.size.height;
  }
  self.webView.frame = webViewFrame;
}

- (void)deviceOrientationChange:(NSNotification *)notification {
  [self resizeViews];
}

#pragma mark GADBannerViewDelegate implementation

- (void)adViewDidReceiveAd:(GADBannerView *)adView {
  NSLog(@"%s: Received ad successfully.", __PRETTY_FUNCTION__);
  [self writeJavascript:@"cordova.fireDocumentEvent('onReceiveAd');"];
}

- (void)adView:(GADBannerView *)view
    didFailToReceiveAdWithError:(GADRequestError *)error {
  NSLog(@"%s: Failed to receive ad with error: %@",
           __PRETTY_FUNCTION__, [error localizedFailureReason]);
  // Since we're passing error data back through Cordova, we need to set this
  // up.
  NSString *jsString =
      @"cordova.fireDocumentEvent('onFailedToReceiveAd',"
      @"{ 'error': '%@' });";
  [self writeJavascript:[NSString stringWithFormat:jsString,
                             [error localizedFailureReason]]];
}

- (void)adViewWillPresentScreen:(GADBannerView *)adView {
  [self writeJavascript:
      @"cordova.fireDocumentEvent('onPresentScreen');"];
}

- (void)adViewDidDismissScreen:(GADBannerView *)adView {
  [self writeJavascript:
      @"cordova.fireDocumentEvent('onDismissScreen');"];
}

- (void)adViewWillLeaveApplication:(GADBannerView *)adView {
  [self writeJavascript:
      @"cordova.fireDocumentEvent('onLeaveApplication');"];
}

#pragma mark Cleanup

- (void)dealloc {
  [[UIDevice currentDevice] endGeneratingDeviceOrientationNotifications];
  [[NSNotificationCenter defaultCenter]
      removeObserver:self
                name:UIDeviceOrientationDidChangeNotification
              object:nil];
  bannerView_.delegate = nil;
  [bannerView_ release];
  [super dealloc];
}

@end
