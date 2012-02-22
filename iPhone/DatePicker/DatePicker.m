//	Phonegap DatePicker Plugin
//	Copyright (c) Greg Allen 2011
//	MIT Licensed
//
//  Additional refactoring by Sam de Freyssinet

#import "DatePicker.h"

@interface DatePicker (Private)

// Initialize the UIActionSheet with ID <UIActionSheetDelegate> delegate
- (void)initActionSheet:(id <UIActionSheetDelegate>)delegateOrNil;

// Initialize the UISegmentedControl with UIView parentView, NSString title, ID target and SEL action
- (void)initActionSheetCloseButton:(UIView *)parentView title:(NSString *)title target:(id)target action:(SEL)action;

// Initialize the UIDatePicker with NSMutableDictionary options
- (UIDatePicker *)createDatePicker:(CGRect)pickerFrame options:(NSMutableDictionary *)optionsOrNil;

@end

@implementation DatePicker

@synthesize datePickerSheet = _datePickerSheet;
@synthesize datePicker = _datePicker;

#pragma mark - Public Methods

- (PGPlugin *)initWithWebView:(UIWebView *)theWebView
{
	self = (DatePicker *)[super initWithWebView:theWebView];

	[self initActionSheet:self];

	[self initActionSheetCloseButton:self.datePickerSheet title:@"Close" target:self action:@selector(dismissActionSheet:)];

	return self;
}

- (void)show:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
	if (isVisible) {
		return;        
	}

	UIDatePicker *datePickerInstance = [[self createDatePicker:CGRectMake(0, 40, 0, 0) options:options] retain];
	self.datePicker = datePickerInstance;
	[self.datePickerSheet addSubview:datePickerInstance];
	[datePickerInstance release];

	[self.datePickerSheet showInView:[[super webView] superview]];	
	[self.datePickerSheet setBounds:CGRectMake(0, 0, 320, 485)];

	isVisible = YES;
}

- (void)dismissActionSheet:(id)sender {
	[self.datePickerSheet dismissWithClickedButtonIndex:0 animated:YES];
}

- (void) onMemoryWarning
{
	// It could be better to close the datepicker before the system
	// clears memory. But in reality, other non-visible plugins should
	// be tidying themselves at this point. This could cause a fatal
	// at runtime.
	if (isVisible) {
		return;
	}

	[self release];
}

- (void)dealloc
{
	[_datePicker release];
	[_datePickerSheet release];

	[super dealloc];
}

#pragma mark - UIActionSheetDelegate methods

- (void)actionSheet:(UIActionSheet *)actionSheet willDismissWithButtonIndex:(NSInteger)buttonIndex
{
	NSString* jsCallback = [NSString stringWithFormat:@"window.plugins.datePicker._dateSelected(\"%i\");", (int)[self.datePicker.date timeIntervalSince1970]];
	NSLog(@"Date: %@", self.datePicker);
	NSLog(@"%f", [self.datePicker.date timeIntervalSince1970]);
	[super writeJavascript:jsCallback];
}

- (void)actionSheet:(UIActionSheet *)actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
	isVisible = NO;
}

#pragma mark - Private Methods

- (void)initActionSheetCloseButton:(UIView *)parentView title:(NSString *)title target:(id)target action:(SEL)action
{
	UISegmentedControl *closeButton = [[UISegmentedControl alloc] initWithItems:[NSArray arrayWithObject:title]];

	closeButton.momentary = YES; 
	closeButton.frame = CGRectMake(260, 7.0f, 50.0f, 30.0f);
	closeButton.segmentedControlStyle = UISegmentedControlStyleBar;
	closeButton.tintColor = [UIColor blackColor];

	[closeButton addTarget:target action:action forControlEvents:UIControlEventValueChanged];
	[parentView addSubview:closeButton];

	[closeButton release];
}

- (void)initActionSheet:(id <UIActionSheetDelegate>)delegateOrNil
{
	UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:nil
															 delegate:delegateOrNil 
													cancelButtonTitle:nil 
											   destructiveButtonTitle:nil 
													otherButtonTitles:nil];

	[actionSheet setActionSheetStyle:UIActionSheetStyleBlackTranslucent];
	self.datePickerSheet = actionSheet;

	[actionSheet release];
}

- (UIDatePicker *)createDatePicker:(CGRect)pickerFrame options:(NSMutableDictionary *)optionsOrNil
{
	NSString *mode = [optionsOrNil objectForKey:@"mode"];
	NSString *dateString = [optionsOrNil objectForKey:@"date"];
	UIDatePicker *datePickerControl = [[UIDatePicker alloc] initWithFrame:pickerFrame];

	BOOL allowOldDates = NO;

	if ([[optionsOrNil objectForKey:@"allowOldDates"] intValue] == 1) {
		allowOldDates = YES;
	}

	if ( ! allowOldDates) {
		datePickerControl.minimumDate = [NSDate date];
	}

	if ([mode isEqualToString:@"date"]) {
		datePickerControl.datePickerMode = UIDatePickerModeDate;

		NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
		[dateFormatter setTimeZone:[NSTimeZone defaultTimeZone]];
		[dateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss'Z'"];
		NSDate *date = [dateFormatter dateFromString:dateString];		
		datePickerControl.date = date;
	}
	else if ([mode isEqualToString:@"time"])
	{
		datePickerControl.datePickerMode = UIDatePickerModeTime;
	}

	return [datePickerControl autorelease];
}

@end
