//
//  Screenshot.h
//
//  Created by Simon Madine on 29/04/2010.
//  Copyright 2010 The Angry Robot Zombie Factory.
//  MIT licensed
//

#import "Screenshot.h"
@implementation Screenshot

- (void)saveScreenshot:(NSArray*)arguments withDict:(NSDictionary*)options
{
	NSUInteger argc = [arguments count];
	NSString* successCallback = nil, *returnBase64 = false;		
	
	if (argc > 0) returnBase64 = [arguments objectAtIndex:0];
	if (argc > 1) successCallback = [arguments objectAtIndex:1];
	
	if (argc < 1) {
		NSLog(@"Screenshot.saveScreenshot: Missing 1st parameter.");
		return;
	}
	
	CGRect screenRect = [[UIScreen mainScreen] bounds];
	CGRect imageRect = CGRectMake(0, 0, CGRectGetWidth(screenRect), CGRectGetHeight(screenRect));
	UIGraphicsBeginImageContext(imageRect.size);
	
	CGContextRef ctx = UIGraphicsGetCurrentContext();
	[[UIColor blackColor] set];
	CGContextTranslateCTM(ctx, 0, 0);
	CGContextFillRect(ctx, imageRect);
	
	[webView.layer renderInContext:ctx];
	
	UIImage *image1 = UIGraphicsGetImageFromCurrentImageContext();
	
	if (returnBase64) {
		if (argc < 2) {
			NSLog(@"Screenshot.saveScreenshot: Missing 2nd parameter.");
			UIGraphicsEndImageContext();
			return;
		} else {
			NSData *imageData = UIImagePNGRepresentation(image1);
			NSString *encodedString = [imageData base64Encoding];
			NSString *jsCallBack;
			jsCallBack = [ NSString stringWithFormat:@"%@(\"%@\");", successCallback, encodedString ];
			[webView stringByEvaluatingJavaScriptFromString:jsCallBack];
		}
	} else {		
		UIImageWriteToSavedPhotosAlbum(image1, nil, nil, nil);
		
		UIAlertView *alert= [[UIAlertView alloc] initWithTitle:nil message:@"Image Saved" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		[alert release];
	}
	
	UIGraphicsEndImageContext();
}
@end