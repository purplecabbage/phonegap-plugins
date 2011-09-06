// HockeyApp.js

//  Created by Owen Brotherwood on 8/24/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
 
/* experimental
 change BWHockeyManager.m defaults and use isUpdateAvailable and initiateAppDownload for another type of user experience
 self.alwaysShowUpdateReminder = NO;
 self.checkForUpdateOnLaunch = NO;
 self.updateSetting = HockeyUpdateCheckManually;
 */


var HockeyApp = (function() {  
    // only works with default HockeyKit defaults
	checkForUpdate = function() {
        PhoneGap.exec("HockeyApp.checkForUpdate");
    },
    
    // provide a function that can be called back with result: ''/1
    isUpdateAvailable = function(cb) {
        PhoneGap.exec("HockeyApp.isUpdateAvailable", GetFunctionName(cb)); 
    },
    
    initiateAppDownload = function(){   // has a return boolean which could be used
        PhoneGap.exec("HockeyApp.initiateAppDownload");
    },
    
    crashTest = function() {
        PhoneGap.exec("HockeyApp.crashTest");
    },
                                
    init = function(args){    
        PhoneGap.exec("HockeyApp.init", args);
    };
    
    return{
        checkForUpdate :    checkForUpdate,
        crashTest :         crashTest,
        isUpdateAvailable : isUpdateAvailable,
        initiateAppDownload : initiateAppDownload,
        init : init
    };

})();
