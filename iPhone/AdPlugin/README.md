Updated for Cordova 1.5 support 2012 - @RandyMcMillan
Add SAiOSAdPlugin SAiOSAdPlugin to the PhoneGap.plist/Cordova.plist under Plugins
Make appropriate edits to SAiOSAdPlugin.js for PhoneGap 1.4.1 support and before

//PhoneGap.exec("SAiOSAdPlugin.orientationChanged", window.orientation);
   Cordova.exec("SAiOSAdPlugin.orientationChanged", window.orientation);

Include SAiOSAdPlugin.js in your www folder (blue color)
Add iAd.framework to Frameworks weaklink/optional
Your App must be added to https://iad.apple.com/itcportal/ to make $

# PhoneGap AdPlugin #
by Shazron Abdullah

## Adding the Plugin to your project ##

Using this plugin requires [iPhone PhoneGap](http://github.com/phonegap/phonegap-iphone) and iAd. iAd requires the [iOS 4 SDK](http://developer.apple.com/iphone).

1. Make sure your PhoneGap Xcode project has been [updated for the iOS 4 SDK](http://wiki.phonegap.com/Upgrade-your-PhoneGap-Xcode-Template-for-iOS-4)
2. Add the "iAd" framework to your Frameworks folder, and set it to be weak linked (see "Weak Linking the iAd Framework" section below)
3. Add the .h and .m files to your Plugins folder in your project
4. Add the .js files to your "www" folder on disk, and add reference(s) to the .js files as <link> tags in your html file(s)
5. See the sample index.html
6. Make sure you check the "RELEASE NOTES" section below!

## Weak Linking the iAd Framework ##

1. In your project, under "Targets", double click on the Target item
2. Go to the "General" Tab, under "Linked Libraries" 
3. For the iAd Framework, change the value from "Required" to "Weak"

## FUTURE FEATURES ##
* Landscape mode iAds
* Showing of other ad network ads in place of iAds, if no iAds are available

## RELEASE NOTES ##

### 20100712 ###
* Initial release
* Only supports portrait iAd banners
* Not tested on a 3.x device
* WebView initial size being oversized is a PhoneGap Core issue, and is not addressed in this plugin. When an ad is shown, it correctly sizes the WebView, and when the ad is hidden, sets the WebView back to its original size.
* Not tested with any other native UI plugins, like TabBar, and will not play nice with them (until we get a better layout management system/plugin)

### 20110627 ###
* Requires PhoneGap 0.9.6
* Fixed size issues
* Orientation change handling
* Works on iPad 4.x
* Still does not play nice with other native UI plugins like TabBar (yet)

## BUGS AND CONTRIBUTIONS ##

Patches welcome! Send a pull request. Since this is not a part of PhoneGap Core (which requires a CLA), this should be easier.

Post issues in the [PhoneGap Google Groups](http://groups.google.com/group/phonegap), include in the subject heading - "AdPlugin" or on [Github](http://github.com/phonegap/phoneGap-plugins/issues)
(preferred)

The latest code (my fork) will always be [here](http://github.com/shazron/phoneGap-plugins/tree/master/iPhone/AdPlugin/)

## LICENSE ##

The MIT License

Copyright (c) 2010 Shazron Abdullah

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

