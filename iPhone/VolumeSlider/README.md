VolumeSlider
============

Installation
------------

This plugin allows you to add a native volume slider (MPVolumeView) to your app.

Add the plugin much like any other:

1.      Add the VolumeSlider.h and VolumeSlider.m classes to your Plugins folder in Xcode
2.      Add the VolumeSlider.js file to your www folder
3.	Add the VolumeSlider.js to your html file. eg: `<script type="text/javascript" charset="utf-8" src="VolumeSlider.js"></script>`
4.      Add the plugin to the PhoneGap.plist
5.      Here is where it differs slightly, [add the MediaPlayer.framework to your project in Xcode](http://paikialog.wordpress.com/2011/03/09/xcode-4how-to-add-framework-into-project/ "Xcode 4–How to add Framework into project?") (this is where MPVolumeView comes from).

### Example
```javascript
function onDeviceReady()
{
	var volumeSlider = window.plugins.volumeSlider;
	volumeSlider.createVolumeSlider(10,350,300,30); // origin x, origin y, width, height
	volumeSlider.showVolumeSlider();
	
	// if not calling showVolumeSlider() right after creating the slider,
	// make sure to call hideVolumeSlider(), so that the "ghost" slider won't block user 
	// from interacting with the component below
	//
	// volumeSlider.hideVolumeSlider();
}
```

... now since this is a native control added more or less on top of your webView, you might have to show and hide it if you navigate away from the _page_ you want the VolumeSlider on:

```javascript
volumeSlider.hideVolumeSlider();
```

## License

The MIT License

Copyright (c) 2011 Tommy-Carlos Williams (github.com/devgeeks)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
