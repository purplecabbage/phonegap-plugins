//  Created by Jesse MacFadyen on 10-05-29.
//  Copyright 2010 Nitobi. All rights reserved.
//  Copyright 2012, Randy McMillan

#import <UIKit/UIKit.h>

@protocol ChildBrowserDelegate<NSObject>


/*
 *  onChildLocationChanging:newLoc
 *
 *  Discussion:
 *    Invoked when a new page has loaded
 */
-(void) onChildLocationChange:(NSString*)newLoc;
-(void) onOpenInSafari;
-(void) onClose;
@end


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
    id <ChildBrowserDelegate> delegate;
}

@property (nonatomic, retain)id <ChildBrowserDelegate> delegate;
@property (nonatomic, retain) NSArray* supportedOrientations;
@property(retain) NSString* imageURL;
@property(assign) BOOL isImage;

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation) interfaceOrientation;
- (ChildBrowserViewController*)initWithScale:(BOOL)enabled;
- (IBAction)onDoneButtonPress:(id)sender;
- (IBAction)onSafariButtonPress:(id)sender;
- (void)loadURL:(NSString*)url;
-(void)closeBrowser;

@end
