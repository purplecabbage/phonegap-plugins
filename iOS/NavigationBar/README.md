Navigation bar for Cordova on iOS
=================================

This plugin lets you create and control a native navigation bar and its buttons.

License
-------

[MIT license](http://www.opensource.org/licenses/mit-license.html)

Contributors
------------

This plugin was put together from the incomplete NativeControls plugin and other sources. See NavigationBar.m for the history.

Installing the plugin
---------------------

- Copy *.xib, *.m and *.h files to your project's "Plugins" folder (should already exist and contain a README file if you used the Cordova project template)
- They have to be added to the project as well, so drag them from the "Plugins" folder (in Finder) to the same folder (in Xcode) and select to create references
- Open "Supporting Files/Cordova.plist" and under "Plugins", add a key with the plugin name "NavigationBar" and a string value of "NavigationBar" (I guess it's the plugin's main class name)

Example
-------

This example shows how to use the navigation bar:

    document.addEventListener("deviceready", function() {
        console.log("Cordova ready")

        plugins.navigationBar.createNavBar()
        plugins.navigationBar.hideLeftNavButton()
        plugins.navigationBar.hideRightNavButton()

        plugins.navigationBar.setNavBarTitle("My heading")

        plugins.navigationBar.showLeftNavButton()
        plugins.navigationBar.showRightNavButton()

        // Create left navigation button with a title (you can either have a title or an image, not both!)
        plugins.navigationBar.setupLeftNavButton("Text", null, function() {
            alert("left nav button tapped")
        })

        // Create right navigation button from a system-predefined button (see the full list in NativeControls.m)
        // or from an image
        plugins.navigationBar.setupRightNavButton(
            null,
            "barButton:Bookmarks", // or your own file like "/www/stylesheets/images/ajax-loader.png",
            function() {
                alert("right nav button tapped")
            }
        )

        plugins.navigationBar.showNavBar()

        window.addEventListener("resize", function() { plugins.navigationBar.resizeTabBar(); }, false);
    }, false)

Reporting issues or requests for improvement
--------------------------------------------

Please report problems on [my GitHub fork of phonegap-plugins](https://github.com/AndiDog/phonegap-plugins).