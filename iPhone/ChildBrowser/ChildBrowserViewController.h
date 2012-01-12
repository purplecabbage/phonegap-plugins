//
//  ChildBrowserViewController.h
//
//  Created by Jesse MacFadyen on 21/07/09.
//  Copyright 2009 Nitobi. All rights reserved.
//

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
  IBOutlet UIBarButtonItem* documentBtn;
	IBOutlet UIActivityIndicatorView* spinner;
  UIDocumentInteractionController* docController;
	BOOL scaleEnabled;
	BOOL isImage;
	NSString* imageURL;
	NSArray* supportedOrientations;
	id <ChildBrowserDelegate> delegate;
  id <UIDocumentInteractionControllerDelegate> docControllerDelegate;
}

@property (retain) UIDocumentInteractionController* docController;
@property (nonatomic, retain)id <ChildBrowserDelegate> delegate;
@property (nonatomic, retain) 	NSArray* supportedOrientations;
@property(retain) NSString* imageURL;
@property(assign) BOOL isImage;

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation) interfaceOrientation; 
- (ChildBrowserViewController*)initWithScale:(BOOL)enabled;
- (IBAction)onDoneButtonPress:(id)sender;
- (IBAction)onSafariButtonPress:(id)sender;
- (IBAction)onDocumentButtonPress:(id)sender;
- (UIDocumentInteractionController *)setupControllerWithURL:(NSURL *)fileURL usingDelegate:(id <UIDocumentInteractionControllerDelegate>)interactionDelegate;
- (void)loadURL:(NSString*)url;
- (void)closeBrowser;

@end
