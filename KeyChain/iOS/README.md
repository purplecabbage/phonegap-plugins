# PhoneGap Keychain-Plugin #
by Shazron Abdullah

## Adding the Plugin to your project ##

Using this plugin requires [iPhone PhoneGap](http://github.com/phonegap/phonegap-iphone).

1. Make sure your PhoneGap Xcode project has been [updated for the iOS 4 SDK](http://wiki.phonegap.com/Upgrade-your-PhoneGap-Xcode-Template-for-iOS-4)
2. Add the "Security.framework" to your Frameworks group in Xcode
3. Add the .h and .m files to your Plugins folder in your project
4. Add the .js files to your "www" folder on disk, and add reference(s) to the .js files as <link> tags in your html file(s)
5. See the sample project "KeychainPlugin-Host" for an example use (examine the code in www/index.html)
6. Make sure you check the "RELEASE NOTES" section below!

## RELEASE NOTES ##

### 20101105 ###
* Initial release
* See the .js file for API docs, and the KeychainPlugin-Host/www/index.html for sample code

## BUGS AND CONTRIBUTIONS ##

Patches welcome! Send a pull request. Since this is not a part of PhoneGap Core (which requires a CLA), this should be easier.

Post issues in the [PhoneGap Google Groups](http://groups.google.com/group/phonegap), include in the subject heading - "KeychainPlugin" or on [Github](http://github.com/phonegap/phonegap-plugins/issues)
(preferred)

The latest code (my fork) will always be [here](http://github.com/shazron/phonegap-plugins/tree/master/iPhone/Keychain/)

## LICENSE ##

SFHFKeychainUtils code by:
  Created by Buzz Andersen on 10/20/08.
  Based partly on code by Jonathan Wight, Jon Crosby, and Mike Malone.
  Copyright 2008 Sci-Fi Hi-Fi. All rights reserved.

The rest:

The MIT License

Copyright (c) 2010 Shazron Abdullah

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

