//
//  ClipboardPlugin.m
//  Clipboard plugin for PhoneGap
//
//  Copyright 2010 Michel Weimerskirch.
//

#import <Foundation/Foundation.h>
#import "PhoneGapCommand.h"
#import "ClipboardPlugin.h"

@implementation ClipboardPlugin

-(void)setText:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options {
	UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
	NSString     *text       = [arguments objectAtIndex:0];

	[pasteboard setValue:text forPasteboardType:@"public.utf8-plain-text"];
}

-(void)getText:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options {
	UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
	NSString     *jsCallback = [arguments objectAtIndex:0];

	NSString *text = [pasteboard valueForPasteboardType:@"public.utf8-plain-text"];
  
  NSString* jsString = [[NSString alloc] initWithFormat:@"%@(\"%@\");", jsCallback, text];
  [webView stringByEvaluatingJavaScriptFromString:jsString];
  
	[jsString release];
}

@end
