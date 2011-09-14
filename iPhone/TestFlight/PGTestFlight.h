//
//  PGTestFlight.h
//  TestFlightTest
//
//  Created by Shazron Abdullah on 11-09-13.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <PhoneGap/PGPlugin.h>

@interface PGTestFlight : PGPlugin

//- (void)addCustomEnvironmentInformation:(NSString *)information forKey:(NSString*)key;
- (void)addCustomEnvironmentInformation:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

//- (void)takeOff:(NSString *)teamToken;
- (void)takeOff:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

//- (void)setOptions:(NSDictionary*)options;
- (void)setOptions:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

//- (void)passCheckpoint:(NSString *)checkpointName;
- (void)passCheckpoint:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

// no args
- (void)openFeedbackView:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;;

@end
