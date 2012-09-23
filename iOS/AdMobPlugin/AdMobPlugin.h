#import <Cordova/CDVPlugin.h>
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "GADBannerViewDelegate.h"

#pragma mark - JS requestAd options

#define KEY_PUBLISHER_ID_ARG    @"publisherId"
#define KEY_AD_SIZE_ARG         @"adSize"
#define KEY_POSITION_AT_TOP_ARG @"positionAtTop"
#define KEY_EXTRAS_ARG          @"extras"
#define KEY_IS_TESTING_ARG      @"isTesting"

@class GADBannerView;

#pragma mark AdMob Plugin

@interface AdMobPlugin : CDVPlugin <GADBannerViewDelegate> {
 @private
  // Value set by the javascript to indicate whether the ad is to be positioned
  // at the top or bottom of the screen.
  BOOL positionAdAtTop_;
}

@property(nonatomic, retain) GADBannerView *bannerView;

- (void)createBannerView:(NSMutableArray *)arguments
                withDict:(NSMutableDictionary *)options;
- (void)requestAd:(NSMutableArray *)arguments
         withDict:(NSMutableDictionary *)options;

@end
