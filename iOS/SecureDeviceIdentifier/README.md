com.apps-pi.securedeviceidentifier
-----------------------------

###Install to project:
cordova plugin add https://github.com/cemerson/cordova-securedeviceidentifier.git

###Remove from project:
cordova plugin rm org.apache.cordova.plugins.SecureDeviceIdentifier

###Changes
- Removed All platforms except IOS for now :(
- Updated plugin files to work in CDV 3 CLI
- Updated SecureDeviceIdentifier.m to eliminate Background Thread warning in Xcode (reference: http://goo.gl/JokRZw)

###TODO:
- If InAppBrowser is open in windowed mode and is called again to be fullscreen need to reset plugin to make sure fullscreen takes effect
- cleanup (never ends)