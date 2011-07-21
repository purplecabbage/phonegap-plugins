# PhoneGap Torch Plugin #
by Shazron Abdullah

## Adding the Plugin to your project ##

Using this plugin requires [iOS PhoneGap](http://github.com/phonegap/phonegap-iphone) and AVFoundation. AVFoundation requires the [iOS 4 SDK](http://developer.apple.com/ios).

1. Add the "AVFoundation" framework to your project, and set it to be weak linked (see "Weak Linking the AVFoundation Framework" section below). 
2. Add the .h and .m files to your Plugins folder in your project
3. Add the .js files to your "www" folder on disk, and add reference(s) to the .js files as <link> tags in your html file(s)
4. See the sample code in "index.html" for an example use

## Weak Linking the AVFoundation Framework ##

**Xcode 3**

1. In your project, under "Targets", double click on the Target item
2. Go to the "General" Tab, under "Linked Libraries" 
3. For the AVFoundation Framework, change the value from "Required" to "Weak"

**Xcode 4**

1. In your project, select your blue project icon in the Project Navigator
2. On the right pane, select the "Build Phases" tab
3. Expand the "Link Binary with Libraries" item
4. For the AVFoundation Framework, change the value from "Required" to "Optional"

## RELEASE NOTES ##

### 20110527 ###
* Initial release
* iOS devices with no flash/torch support will just fail gracefully
* Based off code [here](http://stackoverflow.com/questions/3190034/turn-on-torch-flash-on-iphone-4)

## BUGS ##

Post issues in the [PhoneGap Google Groups](http://groups.google.com/group/phonegap), include in the subject heading - "TorchPlugin" or on [Github](http://github.com/phonegap/phonegap-plugins/issues) (preferred)


## LICENSE ##

The MIT License

Copyright (c) 2011 Shazron Abdullah

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.



