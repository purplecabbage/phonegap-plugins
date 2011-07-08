//
//  Screenshot.h
//
//  Created by Simon Madine on 29/04/2010.
//  Copyright 2010 The Angry Robot Zombie Factory.
//  MIT licensed
//
//  Modifications to support orientation change by @ffd8
//

#import "Screenshot.h"
@implementation Screenshot

- (void)saveScreenshot:(NSArray*)arguments withDict:(NSDictionary*)options
{

    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGRect imageRect;

    //change with orientation
    UIInterfaceOrientation orientation = [[UIDevice currentDevice] orientation];
    if (orientation == UIInterfaceOrientationPortrait || orientation == UIInterfaceOrientationPortraitUpsideDown) { 
            //portrait check
            imageRect = CGRectMake(0, 0, CGRectGetWidth(screenRect), CGRectGetHeight(screenRect));
        }else if(orientation == UIInterfaceOrientationLandscapeLeft || orientation == UIInterfaceOrientationLandscapeRight) { 
            //landscape check
            imageRect = CGRectMake(0, 0, CGRectGetHeight(screenRect), CGRectGetWidth(screenRect));
        }else{
            //default with portrait (after tests.. worked best with this code here)
            imageRect = CGRectMake(0, 0, CGRectGetWidth(screenRect), CGRectGetHeight(screenRect));
        }


    UIGraphicsBeginImageContext(imageRect.size);

    CGContextRef ctx = UIGraphicsGetCurrentContext();
    [[UIColor blackColor] set];
    CGContextTranslateCTM(ctx, 0, 0);
    CGContextFillRect(ctx, imageRect);

    [webView.layer renderInContext:ctx];

    UIImage *image1 = UIGraphicsGetImageFromCurrentImageContext();
    UIImageWriteToSavedPhotosAlbum(image1, nil, nil, nil);
    UIGraphicsEndImageContext();
    UIAlertView *alert= [[UIAlertView alloc] initWithTitle:nil message:@"Image Saved" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
    [alert show];
    [alert release];
}
@end