This plugin is a different philosophy to how contacts can work with PhoneGap Android. Rather than pulling in the contacts into HTML, this plugin allows you to leave the PhoneGap webview and enter into a native contact picker. Once the user has selected a contact, they will be sent back into the PhoneGap webview with the name of the contact available.


How to use:

Put the following javascript code in your initializer, after the DOM has loaded:

document.querySelector("#name-contact-to-native").addEventListener("touchstart", function() {
    	window.plugins.contactView.show("contact-name-from-native");
}, false);

"#name-contact-to-native" is the element id that triggers the contact view.

"contact-name-from-native" is the element id in html that receives the contact's name. By convention, it is an input field's value that receives it. But you can edit this in ContactView.js

For the current files to work, you'll need to create a package (folders) called com.rearden. You can change this to whatever you like, just update the ContactView.js and ContactView.java.

ContactView.js should go in the asset folder and should be referenced in your index.html file.


Limitations:

Currently, only the name is grabbed. I'd like to have future versions include telephone number and/or email addresses.