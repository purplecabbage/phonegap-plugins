//
//  networkActivityIndicator.h
//  
//
//  Created by Tue Topholm on 16/03/11.
//  Copyright 2011 Sugee. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"

@interface networkActivityIndicator : PhoneGapCommand {

}
-	(void) setIndicator:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
