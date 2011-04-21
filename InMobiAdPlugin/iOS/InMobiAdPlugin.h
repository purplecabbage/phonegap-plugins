//
//  InMobiAdPlugin.h
//  InMobiAdTester
//
//  Created by Jesse MacFadyen on 11-04-14.
//  Copyright 2011 Nitobi. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"
#import "InMobiAdView.h"
#import "InMobiEnumTypes.h"
#import "InMobiAdDelegate.h"




@interface InMobiAdPlugin : PhoneGapCommand<InMobiAdDelegate> {

	NSString *_siteId;
	BOOL _atBottom;
	InMobiAdView *inmobiAdView;
	NSMutableDictionary* metaDict;
}

@property (nonatomic, copy)   NSString *_siteId;
@property (assign)			  BOOL _atBottom;
@property (nonatomic,retain)  InMobiAdView *inmobiAdView;
@property (nonatomic, retain) NSMutableDictionary* metaDict;


- (void) init:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) showAd:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;



@end
