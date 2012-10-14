//
//  SocialFrameworkPlugin.h
//  SocialFrameworkPlugin
//
//  Created by Clay Ewing on 9/27/12.
//
//  I basically hacked this together through a tutorial by Andrew Trice: http://www.adobe.com/devnet/html5/articles/extending-phonegap-with-native-plugins-for-ios.html


#import <Cordova/CDV.h>
#import "social/Social.h"
#import "accounts/Accounts.h"

@interface SocialFrameworkPlugin : CDVPlugin {
   
}

@property(nonatomic,copy) NSArray *excludedActivityTypes;

-(void) show:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
-(void) tweet:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
-(void) postToFacebook:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;


@end
