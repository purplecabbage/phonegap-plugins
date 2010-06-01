//
//  ChildBrowserViewController.h
//  DeqqApp
//
//  Created by Jesse MacFadyen on 21/07/09.
//  Copyright 2009 Nitobi. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ChildBrowserViewController : UIViewController < UIWebViewDelegate > {
	IBOutlet UIWebView* webView;
	IBOutlet UIBarButtonItem* closeBtn;
	IBOutlet UIBarButtonItem* refreshBtn;
	IBOutlet UILabel* addressLabel;
	IBOutlet UIBarButtonItem* backBtn;
	IBOutlet UIBarButtonItem* fwdBtn;
	IBOutlet UIBarButtonItem* safariBtn;
	IBOutlet UIActivityIndicatorView* spinner;
	BOOL scaleEnabled;
	BOOL isImage;
	NSString* imageURL;
	NSArray* supportedOrientations;
}

@property (nonatomic, retain) 	NSArray* supportedOrientations;
@property(retain) NSString* imageURL;
@property(assign) BOOL isImage;

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation) interfaceOrientation; 
- (ChildBrowserViewController*)initWithScale:(BOOL)enabled;
- (IBAction)onDoneButtonPress:(id)sender;
- (IBAction)onSafariButtonPress:(id)sender;
- (void)loadURL:(NSString*)url;

@end
