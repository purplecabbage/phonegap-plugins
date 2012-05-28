#import <AudioToolbox/AudioServices.h>
#ifdef PHONEGAP_FRAMEWORK
#import <PhoneGap/PGPlugin.h>
#else
#import "PGPlugin.h"
#endif

@interface SoundPlug : PGPlugin {
}

- (void) play:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
