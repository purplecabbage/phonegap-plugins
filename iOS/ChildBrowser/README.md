The ChildBrowser allows you to display external webpages within your [PhoneGap](http://phonegap.com/) application.

- For back support reference:<br> 
[https://github.com/phonegap/phonegap-plugins/tree/master/iPhone/ChildBrowser 
](https://github.com/phonegap/phonegap-plugins/tree/master/iPhone/ChildBrowser)


A simple use case would be:

- Users can follow links/buttons to view web content without leaving your app. 
- Display web pages/images/videos/pdfs in the ChildBrowser.

This command creates a popup browser that is shown in front of your app, when the user presses the DONE button they are simply returned to your app ( actually they never left ).

The ChildBrowser has buttons for refreshing, navigating back + forwards, as well as the option to open in Safari.

Icons are located in the [ChildBrowser.bundle](https://github.com/phonegap/phonegap-plugins/tree/master/iOS/ChildBrowser/ChildBrowser.bundle) and can be customized. Added Retina -72@2x.png image support to fix small icons in the webview toolbar when displayed on Retina devices.

- Added Temporary Scope (self executing) per [Cordova Plugin Upgrade Guide](https://github.com/phonegap/phonegap-plugins/blob/master/iOS/README.md).




- add Key **ChildBrowserCommand** and Value **ChildBrowserCommand** to the Cordova.plist in your application xcode project.

![image](https://github.com/phonegap/phonegap-plugins/raw/master/iOS/ChildBrowser/Cordova.plist.png)

Here is a sample command to open Google in a ChildBrowser :

cordova.exec("ChildBrowserCommand.showWebPage", "http://www.google.com" );


###Sample use:

    
    var root = this;

    /* When this function is called, PhoneGap has been initialized and is ready to roll */
    function onDeviceReady()
    {
    var cb = ChildBrowser.install();
        if(cb != null)
        {
            cb.onLocationChange = function(loc){ root.locChanged(loc); };
            cb.onClose = function(){root.onCloseBrowser()};
            cb.onOpenExternal = function(){root.onOpenExternal();};

            window.plugins.childBrowser.showWebPage("http://google.com");

        }
    }

    function onCloseBrowser()
    {
        alert("In index.html child browser closed");
    }

    function locChanged(loc) {
        alert("In index.html new loc = " + loc);
        }

    function onOpenExternal() {
        alert("In index.html onOpenExternal");
    }

