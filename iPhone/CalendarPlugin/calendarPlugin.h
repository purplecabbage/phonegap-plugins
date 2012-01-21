//
//  calendarPlugin.m
//  Author: Felix Montanez
//  Date: 01-17-2011
//  Notes: This is my first attempt at creating a plugin for phonegap using 
//  the EventUI framework. I know my code is sloppy and may have errors however
//  it seems to add the my test Dr's appt to my phone calendar.
//  I'm stuck with passing variables from Javascript to Objective-C in order
//  to add events dynamically through the app.

#import <Foundation/Foundation.h>
#ifdef PHONEGAP_FRAMEWORK
#import <PhoneGap/PGPlugin.h>
#else
#import "PGPlugin.h"
#endif
#import <EventKitUI/EventKitUI.h>
#import <EventKit/EventKit.h>

@interface calendarPlugin : PGPlugin <EKEventEditViewDelegate> {
    
	EKEventStore *eventStore;
    EKCalendar *defaultCalendar;
    
}

@property (nonatomic,retain) EKEventStore *eventStore;
@property (nonatomic,retain) EKCalendar *defaultCalendar;


// Calendar Instance methods
- (void)createEvent:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end