//
//  localizable.h
//  
//
//  Created by Tue Topholm on 13/02/11.
//  Copyright 2011 Sugee. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"

@interface localizable : PhoneGapCommand {}
-	(void) get:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
@end
