/*
 *  This code is adapted from the work of Michael Nachbaur 
 *  by Simon Madine of The Angry Robot Zombie Factory
 *   - Converted to Cordova 1.6.1 by Joseph Stuhr.
 *  2012-04-19
 *  MIT licensed
 *
 */

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

@interface Badge : CDVPlugin {
}
- (void)setBadge:(CDVInvokedUrlCommand*)command;

@end