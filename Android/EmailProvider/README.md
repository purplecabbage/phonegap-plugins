# Email Provider plugin for Phonegap #
This plugin create email send request to android device.
Email Provider created and edited by Ray Ling

## Adding the Plugin to your project ##

1. Copy EmailProvider.java to your project src/com.phonegap/plugin/EmailProvider.java
2. emailprovider.js should go in your android project assets, and add script reference in your html
3. call plugin apis

## Usage ##

### Add contact ###

window.plugins.emailprovider.send(email, subject, content, success);

Navigatee to native email send provider on the mobile, whatever the operation customer did, always call success callback.

Example:

    document.querySelector("#send-feedback-button").addEventListener("touchstart", function(){
        window.plugins.emailprovider.add( "feedback@company.com",
            "Feedback to product",
            function(){
                alert("We are back.");
            }
        );
    }, false);
