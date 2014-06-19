#import <Cordova/CDV.h>
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "GADBannerViewDelegate.h"

#pragma mark - JS requestAd options

#define PUBLISHER_ID_ARG_INDEX    0
#define AD_SIZE_ARG_INDEX         1
#define POSITION_AT_TOP_ARG_INDEX 2
#define IS_TESTING_ARG_INDEX      0
#define EXTRAS_ARG_INDEX          1

@class GADBannerView;

#pragma mark AdMob Plugin

// This version of the AdMob plugin has been tested with Cordova version 2.5.0.
@interface AdMobPlugin : CDVPlugin <GADBannerViewDelegate> {
 @private
  // Value set by the javascript to indicate whether the ad is to be positioned
  // at the top or bottom of the screen.
  BOOL positionAdAtTop_;
}

@property(nonatomic, retain) GADBannerView *bannerView;

- (void)createBannerView:(CDVInvokedUrlCommand *)command;
- (void)requestAd:(CDVInvokedUrlCommand *)command;

@end
