//
//  Geofencing.h
//  TikalTimeTracker
//  Sections of this code adapted from Apache Cordova
//
//  Created by Dov Goldberg on 5/3/12.
//  Copyright (c) 2012 Ogonium. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

#ifdef CORDOVA_FRAMEWORK
#import <Cordova/CDVPlugin.h>
#else
#import "CDVPlugin.h"
#endif

#define KEY_REGION_ID       @"fid"
#define KEY_PROJECT_LAT     @"latitude"
#define KEY_PROJECT_LNG     @"longitude"

enum DGLocationStatus {
    PERMISSIONDENIED = 1,
    POSITIONUNAVAILABLE,
    TIMEOUT,
    REGIONMONITORINGPERMISSIONDENIED,
    REGIONMONITORINGUNAVAILABLE
};
typedef NSUInteger DGLocationStatus;

// simple ojbect to keep track of location information
@interface DGLocationData : NSObject

@property (nonatomic, assign) DGLocationStatus locationStatus;
@property (nonatomic, retain) CLLocation* locationInfo;
@property (nonatomic, retain) NSMutableArray* locationCallbacks;

@end

@interface Geofencing : CDVPlugin <CLLocationManagerDelegate>

@property (nonatomic, retain) CLLocationManager *locationManager;
@property (nonatomic, retain) DGLocationData* locationData;

- (BOOL) isLocationServicesEnabled;
- (BOOL) isAuthorized;
- (BOOL) isRegionMonitoringAvailable;
- (BOOL) isRegionMonitoringEnabled;
- (void) saveGeofenceCallbackId:(NSString *) callbackId;
- (void) addRegion:(NSMutableDictionary *)params;
- (void) removeRegion:(NSMutableDictionary *)params;

- (void) returnLocationError: (NSUInteger) errorCode withMessage: (NSString*) message;
- (void) returnRegionSuccess;

#pragma mark Plugin Functions
- (void)addRegion:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void)removeRegion:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
