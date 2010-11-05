# File Uploader plugin for Phonegap #
By Matt Kane

## Adding the Plugin to your project ##

1. To install the plugin, move fileuploader.js to your project's www folder and include a reference to it 
in your html files. 
2. Create a folder called "beetight" within your project's src/com/ folder and move the java file into it.

## Using the plugin ##
The plugin creates the object `window.plugins.fileUploader` with two methods, `upload` and `uploadByUri`. 
These are identical except for the format of the reference to the file to upload. `upload` takes an
absolute path, e.g. `/sdcard/media/images/image.jpg`, while `uploadByUri` takes a content:// Uri,
e.g. `content://media/external/images/media/5`.
The full params are as follows:

* server URL of the server that will receive the file
* file Path or uri of the file to upload
* fileKey Object with key: value params to send to the server
* params Parameter name of the file
* fileName Filename to send to the server. Defaults to image.jpg
* mimeType Mimetype of the uploaded file. Defaults to image/jpeg
* callback Success callback. Also receives progress messages during upload.
* fail Error callback

Note that the success callback isn't just called when the upload is complete, but also gets progress
events while the file is uploaded. It's passed an object of the form:

    {
        status: 'PROGRESS', //Either FileUploader.Status.PROGRESS or FileUploader.Status.COMPLETE,
        progress: 0,        //The number of bytes uploaded so far. 
        total: 1000,        //The total number of bytes to upload.
        result: "OK"        //The string returned from the server. Only sent when status = complete.
    }

This is under development, and the API is likely to change.        

	
## BUGS AND CONTRIBUTIONS ##
The latest bleeding-edge version is available [on GitHub](http://github.com/ascorbic/phonegap-plugins/tree/master/Android/)
If you have a patch, fork my repo baby and send me a pull request. Submit bug reports on GitHub, please.
	
## Licence ##

The MIT License

Copyright (c) 2010 Matt Kane

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.




	