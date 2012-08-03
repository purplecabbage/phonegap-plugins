Amazon In-App Purchasing SDK (Phonegap 2.0 Plugin)
==================================================

This project is a Phonegap 2.0 plugin, for the Amazon Appstore for Android's In-App Purchasing SDK.

To get started:

1. Go to developer.amazon.com, and download the In-App purchasing sdk, and the Amazon SDK Tester.
2. adb install -r AmazonSDKTester.apk (the Amazon Appstore SDK Tester)
3. adb push ./assets/amazon.sdktester.json /mnt/sdcard. (Copy the amazon.sdktester.json file in the assets folder to /mnt/sdcard)
4. git clone this project, and build / run it to play with the plugin.

Here is a screenshot representing the purchase flow.

![Purchase flow screenshot](https://github.com/tikurahul/AmazonInAppPurchasing/raw/master/assets/sdk_purchase.png "Screenshot for the purchase flow")

License (The MIT License)
=========================

Copyright (c) 2012 Rahul Ravikumar

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the 'Software'), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.