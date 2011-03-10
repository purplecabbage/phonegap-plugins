//
//  ClipboardPlugin.h
//  Clipboard plugin for PhoneGap
//
//  Copyright 2010 Michel Weimerskirch.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"

@interface ClipboardPlugin : PhoneGapCommand{ }

-(void)setText:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
