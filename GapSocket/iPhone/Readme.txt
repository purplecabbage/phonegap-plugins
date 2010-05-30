This code is dependant on the opensource project:

http://code.google.com/p/cocoaasyncsocket/

You will need to include 
AsyncSocket.h/.m in your project for this to work.

Add GapSocketCommand.m/.h to your project ( in the plugins folder makes the most sense ... )
Add GapSocket.js to your www folder, and include it in your html

Wait for deviceready event before calling or creating a GapSocket!

To create a socket:

var mySocket = new GapSocket(hostOrIP, port);

To be notified when the socket is connected :

mySocket.onopen = function(){ /* do something meaningful with your life! */ };

To listen for incoming messages:

mySocket.onmessage = function(msg){alert(msg);};

To be notified of an error :

mySocket.onerror = function(msg){alert("Oh Shit! " + msg);};

To send data:

mySocket.send("some data here");

To be notified when the socket is closed:

mySocket.onclose = function(){ /* moving right along ... */ };





