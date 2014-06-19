AdMob Cordova Plugin for iOS
================================

This is the AdMob Cordova Plugin for iOS.  It provides a way to request
AdMob ads natively from JavaScript.  This plugin was written and tested with
the Google AdMob SDK version 6.4.0 for iOS, and Cordova 2.5.0.

##Requirements:

- Cordova SDK for iOS
- Cordova JS for iOS
- Google AdMob Ads SDK for iOS
- iOS version 3.2 or later as well as XCode 4.2 or later
- AdMob publisher ID from [AdMob](www.admob.com)

##Setup:

1. Import Cordova SDK binary and Google AdMob SDK binary into your project (with
   their associated header files).
2. Place Cordova JS and AdMobPlugin.js inside www/ folder. This should be where
   your index.html lives.
3. Place AdMobPlugin.h and AdMobPlugin.m into the plugins/ folder.
4. Add AdMobPlugin to Config.xml under the \<plugins\> element. The entry should
   be \<plugin name="AdMobPlugin" value="AdMobPlugin" \/>.
5. To make sure there are no domain whitelisting issues, make sure you've set
   the origin attribute of the \<access\> element to "*".
6. Complete the Google AdMob SDK setup for iOS at
   https://developers.google.com/mobile-ads-sdk/docs.

##Implementation:

There are two calls needed to get AdMob Ads:

1. `createBannerView`

   Takes in a object containing a publisherId and adSize, as well as success
   and failure callbacks.  An example call is provided below:

         window.plugins.AdMob.createBannerView(
             {
               'publisherId': 'INSERT_YOUR_PUBLISHER_ID_HERE',
               'adSize': AdMob.AdSize.BANNER
             },
             successCallback,
             failureCallback
         );

2. `requestAd`

   Takes in an object containing an optional testing flag, and an optional
   list of extras.  This method should only be invoked once createBannerView
   has invoked successCallback.  An example call is provided below:

         logCreateBannerSuccess();
         window.plugins.AdMob.requestAd(
             {
               'isTesting': false,
               'extras': {
                 'color_bg': 'AAAAFF',
                 'color_bg_top': 'FFFFFF',
                 'color_border': 'FFFFFF',
                 'color_link': '000080',
                 'color_text': '808080',
                 'color_url': '008000'
               },
             },
             successCallback,
             failureCallback
         );


This plugin also allows you the option to listen for ad events.  The following
events are supported:

    document.addEventListener('onReceiveAd', callback);
    document.addEventListener('onFailedToReceiveAd', callback);
    document.addEventListener('onDismissScreen', callback);
    document.addEventListener('onPresentScreen', callback);
    document.addEventListener('onLeaveApplication', callback);
