# SocialFrameworkPlugin for Cordova / PhoneGap & iOS 6

by Clay Ewing

This is a really simple plugin to utilize the Social Framework in iOS 6.


## Adding the Plugin to your project

1. Add SocialFrameworkPlugin.m and SocialFrameworkPlugin.h to your plugin directory
2. Add social.js to your www directory
3. Add the Social and Accounts frameworks to your project under Build Settings
4. Add the plugin to the Cordova.plist file under Plugins (key: com.dataplayed.SocialFrameworkPlugin value: SocialFrameworkPlugin)



### Usage
Look at index.html for basic functionality.
There are 3 commands: show, tweet and fbPost
They all work like so:

SocialFrameworkPlugin.show( shareSuccessCallback, shareErrorCallback, textToYouWantToPushToShareActivity );

Show lets the user choose which service to use while tweet and fbPost go directly to their specific providers.

### Release Notes:

* I have noticed in PhoneGap with the iPhone 5 simulator that the icon for Messages doesn't exist.

* If you want to modify what services show up, modify the line 33 in SocialFrameworkPlugin.m:

activityController.excludedActivityTypes = @[UIActivityTypePostToWeibo, UIActivityTypeAssignToContact, UIActivityTypeCopyToPasteboard];

A full list can be found at http://developer.apple.com/library/ios/#documentation/UIKit/Reference/UIActivity_Class/Reference/Reference.html#//apple_ref/occ/cl/UIActivity
