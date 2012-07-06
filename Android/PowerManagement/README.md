PowerManagement
===============
Plugin for Cordova (1.9)

The PowerManagement plugin offers access to the devices power-management functionality.
It should be used for applications which keep running for a long time without any user interaction.

For details on power functionality see:

* Android: [PowerManager](http://developer.android.com/reference/android/os/PowerManager.html)
* iOS: [idleTimerDisabled](http://developer.apple.com/library/ios/documentation/UIKit/Reference/UIApplication_Class/Reference/Reference.html#//apple_ref/occ/instp/UIApplication/idleTimerDisabled)

Platforms
---------
Currently available on:

### Android
Copy the *PowerManagement.java* file to your *src/* directory.

Edit your *AndroidManifest.xml* and add the following permission:
`<uses-permission android:name="android.permission.WAKE_LOCK" />`

In addition you have to edit your *res/xml/plugins.xml* file to let Cordova know about the plugin:
`<plugin name="PowerManagement" value="org.apache.cordova.plugin.PowerManagement"/>`

### iOS
Copy the *PowerManagement.h* and *PowerManagement.m* files to your projects "Plugins" folder.

Add the PowerManagement plugin to the *Cordova.plist* file (to the Plugins list). Both Key and Value are "PowerManagement".


License
=======
   Copyright 2012 Wolfgang Koller - http://www.gofg.at/

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
