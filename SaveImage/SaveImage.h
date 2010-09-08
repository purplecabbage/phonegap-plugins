//
//  SaveImage.h
//
//  Created by MyFreeWeb on 29/04/2010.
//  Copyright 2010 MyFreeWeb.
//  MIT licensed
//

#import <Foundation/Foundation.h>
#import "NSData+Base64.h"
#import "PhoneGapCommand.h"
@interface SaveImage : PhoneGapCommand {
}

- (void)saveImage:(NSMutableArray*)sdata withDict:(NSMutableDictionary*)options;
@end
