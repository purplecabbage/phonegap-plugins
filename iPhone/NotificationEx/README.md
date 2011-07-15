## NotificationEx - Adding the Plugin to your project ##

These are removed functions from PhoneGap core (Notification.loadingStart/Stop, and Notification.activityStart/Stop)
Using this plugin requires [iPhone PhoneGap](http://github.com/phonegap/phonegap-iphone) 1.0.0

1. Add all the .h and .m files to your Plugins folder in your project
2. Add the .js files to your "www" folder on disk, and add a reference to the .js file as &lt;script&gt; tags in your html file(s)
3. In your project's PhoneGap.plist, find the Plugins section. Add a new entry under there, the key is "NotificationEx", value is "NotificationEx".
4. See the sample index.html for usage examples
