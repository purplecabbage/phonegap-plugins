var SocialFrameworkPlugin = {

show: function (success, fail, textToShare) {
    return Cordova.exec( success, fail,
                        "com.dataplayed.SocialFrameworkPlugin",
                        "show",
                        [textToShare]);
},
    
tweet: function (success, fail, textToShare) {
    return Cordova.exec (success, fail, "com.dataplayed.SocialFrameworkPlugin",
                         "tweet", [textToShare]);
},
    
postToFacebook: function (success, fail, textToShare) {
    return Cordova.exec (success, fail, "com.dataplayed.SocialFrameworkPlugin",
                         "postToFacebook", [textToShare]);
}

};
