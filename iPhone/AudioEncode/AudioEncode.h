//
//  AudioEncode.h
//
//  By Lyle Pratt, September 2011.
//  MIT licensed
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>

@interface AudioEncode : CDVPlugin {
    NSString* callback;
}

@property (nonatomic, retain) NSString* callback;

- (void)encodeAudio:(NSArray*)arguments withDict:(NSDictionary*)options;
@end
