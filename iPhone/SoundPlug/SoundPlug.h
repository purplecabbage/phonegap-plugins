#import <AudioToolbox/AudioServices.h>
#import "PhoneGapCommand.h"

@interface SoundPlug : PhoneGapCommand {
}

- (void) play:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end
