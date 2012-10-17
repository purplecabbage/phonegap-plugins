/**
 * Geofencing.js
 *  
 * Phonegap Geofencing Plugin
 * Copyright (c) Dov Goldberg 2012
 * http://www.ogonium.com
 *
 */
var Geofencing = {
	/*
	Params:
	#define KEY_REGION_ID       @"fid"
	#define KEY_PROJECT_NAME    @"projectname"
	#define KEY_PROJECT_LAT     @"latitude"
	#define KEY_PROJECT_LNG     @"longitude"
    #define KEY_PROJECT_ADDRESS @"address"
	*/
     addRegion: function(params, success, fail) {
          return PhoneGap.exec(success, fail, "Geofencing", "addRegion", [params]);
     },

     /*
	Params:
	#define KEY_REGION_ID       @"fid"
	#define KEY_PROJECT_NAME    @"projectname"
	#define KEY_PROJECT_LAT     @"latitude"
    #define KEY_PROJECT_LNG     @"longitude"
    #define KEY_PROJECT_ADDRESS @"address"
	*/
     removeRegion: function(params, success, fail) {
          return PhoneGap.exec(success, fail, "Geofencing", "removeRegion", [params]);
     }
};