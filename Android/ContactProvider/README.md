# Contact Provider plugin for Phonegap #
This plugin is a different philosophy to how contacts can work with PhoneGap Android. Rather than pulling in the contacts into HTML, this plugin allows you to leave the PhoneGap webview and enter into a native contact picker. Once the user has selected a contact, they will be sent back into the PhoneGap webview with the name of the contact available.

Pick Contact function is refered to https://github.com/grandecomplex ContactView.
Contact Provider created and edited by Ray Ling

## Adding the Plugin to your project ##

1. Copy ContactProvider.java to your project src/com.phonegap/plugin/ContactProvider.java
2. contactprovider.js should go in your android project assets, and add script reference in your html
3. call plugin apis

## Usage ##

### Add contact ###

window.plugins.contactprovider.add(contactname, contactemail, success);

Navigatee to native contact provider on the mobile, whatever the operation customer did, always call success callback.

Example:

    document.querySelector("#add-contact-button").addEventListener("touchstart", function(){
        window.plugins.contactprovider.add( document.getElementById("contact-name").value,
            document.getElementById("contact-email").value,
            function(){
                alert("We are back.");
            }
        );
    }, false);

### Pick contact ###

window.plugins.contactprovider.pick(success, failure);

Success returns an object with the name and the phone of the contact selected.

Example:

	document.querySelector("#contact-name-to-native").addEventListener("touchstart", function() {
	    window.plugins.contactprovider.pick(
	    	function(contact) {
	    		document.getElementById("contact-name-from-native").value = contact.name;
	    		document.getElementById("contact-phone").value = contact.phone;
	    	},
	    	function(fail) {
	    		alert("We were unable to get the contact you selected.");
	    	}
	    );
    }, false);
