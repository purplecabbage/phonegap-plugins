//
//  calendarPlugin.m
//  Author: Felix Montanez
//  Date: 01-17-2011
//  Notes: This is my first attempt at creating a plugin for phonegap using 
//  the EventUI framework. I know my code is sloppy and may have errors however
//  it seems to add the Dr's appt to my phone calendar.
//  I'm stuck with passing variables from Javascript to Objective-C in order
//  to add events dynamically through the app.

#import "calendarPlugin.h"
#import <EventKitUI/EventKitUI.h>
#import <EventKit/EventKit.h>

@implementation calendarPlugin
@synthesize eventStore;
@synthesize defaultCalendar;
//@synthesize callbackID;

-(void)createEvent:(NSMutableArray *)arguments withDict:(NSMutableDictionary *)options 
{
    //Get the Event store object
    EKEventStore *store = [[EKEventStore alloc] init];
    EKEvent *myEvent = [EKEvent eventWithEventStore: store];
    
    myEvent.title = @"Doctor's appt";
    myEvent.location = @"Los Angeles";
    myEvent.startDate = [NSDate dateWithTimeIntervalSinceNow:60*60*24];
    myEvent.endDate = [NSDate dateWithTimeIntervalSinceNow:60*60*26];
    myEvent.calendar = store.defaultCalendarForNewEvents;
    
    EKAlarm *reminder = [EKAlarm alarmWithRelativeOffset:-2*60*60];
    
    [myEvent addAlarm:reminder];
    
    NSError *error;
    BOOL saved = [store saveEvent:myEvent span:EKSpanThisEvent
                            error:&error];
    if (saved) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Dr Appt"
                                                        message:@"Saved to Calendar" delegate:self
                                              cancelButtonTitle:@"Sweet!"
                                              otherButtonTitles:nil];
        [alert show];
        [alert release];
    }
}

//delegate method for EKEventEditViewDelegate
-(void)eventEditViewController:(EKEventEditViewController *)controller didCompleteWithAction:(EKEventEditViewAction)action {
    [self dismissModalViewControllerAnimated:YES];
}
@end
