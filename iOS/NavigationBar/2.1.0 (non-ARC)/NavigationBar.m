/*
 NavigationBar.m

 Work based on the NativeControls plugin (Jesse MacFadyen, MIT licensed) and additions made by Hiedi Utley
 (https://github.com/hutley/HelloPhoneGap1.0/) and zSprawl (https://github.com/zSprawl/NativeControls/).

 Navigation bar API cleaned, improved and moved in a separate plugin by Andreas Sommer
 (AndiDog, https://github.com/AndiDog/phonegap-plugins).
 */

#import "NavigationBar.h"
#import <UIKit/UITabBar.h>
#import <QuartzCore/QuartzCore.h>

// For older versions of Cordova, you may have to use: #import "CDVDebug.h"
#import <Cordova/CDVDebug.h>

@implementation NavigationBar
#ifndef __IPHONE_3_0
@synthesize webView;
#endif
@synthesize navBarController;

-(CDVPlugin*) initWithWebView:(UIWebView*)theWebView
{
    self = (NavigationBar*)[super initWithWebView:theWebView];
    if(self)
    {
        // -----------------------------------------------------------------------
        // This code block is the same for both the navigation and tab bar plugin!
        // -----------------------------------------------------------------------

        // The original web view bounds must be retrieved here. On iPhone, it would be 0,0,320,460 for example. Since
        // Cordova seems to initialize plugins on the first call, there is a plugin method init() that has to be called
        // in order to make Cordova call *this* method. If someone forgets the init() call and uses the navigation bar
        // and tab bar plugins together, these values won't be the original web view bounds and layout will be wrong.
        // UPDATE for Cordova 2.1.0: Status bar not considered anymore in the original bounds, so they are 0,0,320,480
        //                           for instance. Now we detect the screen resolution instead and calculate the
        //                           expected value.
        originalWebViewBounds = theWebView.bounds;
        const CGRect screenBounds = [[UIScreen mainScreen] bounds];
        UIApplication *app = [UIApplication sharedApplication];

        if(!app.statusBarHidden)
        {
            float statusBarHeight = MIN(app.statusBarFrame.size.width, app.statusBarFrame.size.height);

            // IMPORTANT: The originalWebViewBounds variable represents the original frame as if the app was started in
            //            portrait mode.
            originalWebViewBounds = CGRectMake(screenBounds.origin.x,
                                               screenBounds.origin.y + statusBarHeight,
                                               screenBounds.size.width,
                                               screenBounds.size.height - statusBarHeight);
        }

        navBarHeight = 44.0f;
        tabBarHeight = 49.0f;
        // -----------------------------------------------------------------------
    }
    return self;
}

- (void)dealloc
{
    if (navBar)
        [navBar release];

    if (navBarController)
        [navBarController release];

    [super dealloc];
}

// NOTE: Returned object is owned
-(UIBarButtonItem*)backgroundButtonFromImage:(NSString*)imageName title:(NSString*)title fixedMarginLeft:(float)fixedMarginLeft fixedMarginRight:(float)fixedMarginRight target:(id)target action:(SEL)action
{
    UIButton *backButton = [[UIButton alloc] init];
    UIImage *imgNormal = [UIImage imageNamed:imageName];

    // UIImage's resizableImageWithCapInsets method is only available from iOS 5.0. With earlier versions, the
    // stretchableImageWithLeftCapWidth is used which behaves a bit differently.
    if([imgNormal respondsToSelector:@selector(resizableImageWithCapInsets)])
        imgNormal = [imgNormal resizableImageWithCapInsets:UIEdgeInsetsMake(0, fixedMarginLeft, 0, fixedMarginRight)];
    else
        imgNormal = [imgNormal stretchableImageWithLeftCapWidth:MAX(fixedMarginLeft, fixedMarginRight) topCapHeight:0];

    [backButton setBackgroundImage:imgNormal forState:UIControlStateNormal];

    backButton.titleLabel.textColor = [UIColor whiteColor];
    backButton.titleLabel.font = [UIFont boldSystemFontOfSize:12.0f];
    backButton.titleLabel.textAlignment = UITextAlignmentCenter;

    CGSize textSize = [title sizeWithFont:backButton.titleLabel.font];

    float buttonWidth = MAX(imgNormal.size.width, textSize.width + fixedMarginLeft + fixedMarginRight);//imgNormal.size.width > (textSize.width + fixedMarginLeft + fixedMarginRight)
                        //? imgNormal.size.width : (textSize.width + fixedMarginLeft + fixedMarginRight);
    backButton.frame = CGRectMake(0, 0, buttonWidth, imgNormal.size.height);

    CGFloat marginTopBottom = (backButton.frame.size.height - textSize.height) / 2;
    [backButton setTitleEdgeInsets:UIEdgeInsetsMake(marginTopBottom, fixedMarginLeft, marginTopBottom, fixedMarginRight)];

    [backButton setTitle:title forState:UIControlStateNormal];
    [backButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [backButton.titleLabel setFont:[UIFont boldSystemFontOfSize:12.0f]];

    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [backButton addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];

    [backButton release];
    // imgNormal is autoreleased

    return backButtonItem;
}

-(void)correctWebViewBounds
{
    if(!navBar)
        return;

    const bool navBarShown = !navBar.hidden;
    bool tabBarShown = false;
    bool tabBarAtBottom = true;

    UIView *parent = [navBar superview];
    for(UIView *view in parent.subviews)
        if([view isMemberOfClass:[UITabBar class]])
        {
            tabBarShown = !view.hidden;

            // Tab bar height is customizable
            if(tabBarShown)
            {
                tabBarHeight = view.bounds.size.height;

                // Since the navigation bar plugin plays together with the tab bar plugin, and the tab bar can as well
                // be positioned at the top, here's some magic to find out where it's positioned:
                tabBarAtBottom = true;
                if([view respondsToSelector:@selector(tabBarAtBottom)])
                    tabBarAtBottom = [view performSelector:@selector(tabBarAtBottom)];
            }

            break;
        }

    // -----------------------------------------------------------------------------
    // IMPORTANT: Below code is the same in both the navigation and tab bar plugins!
    // -----------------------------------------------------------------------------

    CGFloat left = originalWebViewBounds.origin.x;
    CGFloat right = left + originalWebViewBounds.size.width;
    CGFloat top = originalWebViewBounds.origin.y;
    CGFloat bottom = top + originalWebViewBounds.size.height;

    UIInterfaceOrientation orientation = [[UIApplication sharedApplication] statusBarOrientation];
    switch (orientation)
    {
        case UIInterfaceOrientationPortrait:
        case UIInterfaceOrientationPortraitUpsideDown:
            // No need to change width/height from original bounds
            break;
        case UIInterfaceOrientationLandscapeLeft:
        case UIInterfaceOrientationLandscapeRight:
            right = left + originalWebViewBounds.size.height + 20.0f;
            bottom = top + originalWebViewBounds.size.width - 20.0f;
            break;
        default:
            NSLog(@"Unknown orientation: %d", orientation);
            break;
    }

    if(navBarShown)
        top += navBarHeight;

    if(tabBarShown)
    {
        if(tabBarAtBottom)
            bottom -= tabBarHeight;
        else
            top += tabBarHeight;
    }

    CGRect webViewBounds = CGRectMake(left, top, right - left, bottom - top);

    [self.webView setFrame:webViewBounds];

    // -----------------------------------------------------------------------------

    // NOTE: Following part again for navigation bar plugin only

    if(navBarShown)
    {
        if(tabBarAtBottom)
            [navBar setFrame:CGRectMake(left, originalWebViewBounds.origin.y, right - left, navBarHeight)];
        else
            [navBar setFrame:CGRectMake(left, originalWebViewBounds.origin.y + tabBarHeight, right - left, navBarHeight)];
    }
}

/*********************************************************************************/

-(void) create:(CDVInvokedUrlCommand*)command
{
    if(navBar)
        return;

    navBarController = [[CDVNavigationBarController alloc] init];
    navBar = (UINavigationBar*)[navBarController view];

    const NSDictionary *options = command ? [command.arguments objectAtIndex:0] : nil;

    if(options)
    {
        id style = [options objectForKey:@"style"];

        if(style && style != [NSNull null])
        {
            if([style isEqualToString:@"BlackTranslucent"])
                navBar.barStyle = UIBarStyleBlackTranslucent;
            else if([style isEqualToString:@"BlackOpaque"])
                navBar.barStyle = UIBarStyleBlackOpaque;
            else if([style isEqualToString:@"Black"])
                navBar.barStyle = UIBarStyleBlack;
            // else the default will be used
        }

        id tint = [options objectForKey:@"tintColorRgba"];

        if(tint && tint != [NSNull null])
        {
            NSArray *rgba = [tint componentsSeparatedByString:@","];
            navBar.tintColor = [UIColor colorWithRed:[[rgba objectAtIndex:0] intValue]/255.0f
                                               green:[[rgba objectAtIndex:1] intValue]/255.0f
                                                blue:[[rgba objectAtIndex:2] intValue]/255.0f
                                               alpha:[[rgba objectAtIndex:3] intValue]/255.0f];
        }
    }

    [navBarController setDelegate:self];

    [[navBarController view] setFrame:CGRectMake(0, 0, originalWebViewBounds.size.width, navBarHeight)];
    [[[self webView] superview] addSubview:[navBarController view]];
    [navBar setHidden:YES];
}

+ (UIBarButtonSystemItem)getUIBarButtonSystemItemForString:(NSString*)imageName
{
    UIBarButtonSystemItem systemItem = -1;

    if([imageName isEqualToString:@"barButton:Action"])        systemItem = UIBarButtonSystemItemAction;
    else if([imageName isEqualToString:@"barButton:Add"])           systemItem = UIBarButtonSystemItemAdd;
    else if([imageName isEqualToString:@"barButton:Bookmarks"])     systemItem = UIBarButtonSystemItemBookmarks;
    else if([imageName isEqualToString:@"barButton:Camera"])        systemItem = UIBarButtonSystemItemCamera;
    else if([imageName isEqualToString:@"barButton:Cancel"])        systemItem = UIBarButtonSystemItemCancel;
    else if([imageName isEqualToString:@"barButton:Compose"])       systemItem = UIBarButtonSystemItemCompose;
    else if([imageName isEqualToString:@"barButton:Done"])          systemItem = UIBarButtonSystemItemDone;
    else if([imageName isEqualToString:@"barButton:Edit"])          systemItem = UIBarButtonSystemItemEdit;
    else if([imageName isEqualToString:@"barButton:FastForward"])   systemItem = UIBarButtonSystemItemFastForward;
    else if([imageName isEqualToString:@"barButton:FixedSpace"])    systemItem = UIBarButtonSystemItemFixedSpace;
    else if([imageName isEqualToString:@"barButton:FlexibleSpace"]) systemItem = UIBarButtonSystemItemFlexibleSpace;
    else if([imageName isEqualToString:@"barButton:Organize"])      systemItem = UIBarButtonSystemItemOrganize;
    else if([imageName isEqualToString:@"barButton:PageCurl"])      systemItem = UIBarButtonSystemItemPageCurl;
    else if([imageName isEqualToString:@"barButton:Pause"])         systemItem = UIBarButtonSystemItemPause;
    else if([imageName isEqualToString:@"barButton:Play"])          systemItem = UIBarButtonSystemItemPlay;
    else if([imageName isEqualToString:@"barButton:Redo"])          systemItem = UIBarButtonSystemItemRedo;
    else if([imageName isEqualToString:@"barButton:Refresh"])       systemItem = UIBarButtonSystemItemRefresh;
    else if([imageName isEqualToString:@"barButton:Reply"])         systemItem = UIBarButtonSystemItemReply;
    else if([imageName isEqualToString:@"barButton:Rewind"])        systemItem = UIBarButtonSystemItemRewind;
    else if([imageName isEqualToString:@"barButton:Save"])          systemItem = UIBarButtonSystemItemSave;
    else if([imageName isEqualToString:@"barButton:Search"])        systemItem = UIBarButtonSystemItemSearch;
    else if([imageName isEqualToString:@"barButton:Stop"])          systemItem = UIBarButtonSystemItemStop;
    else if([imageName isEqualToString:@"barButton:Trash"])         systemItem = UIBarButtonSystemItemTrash;
    else if([imageName isEqualToString:@"barButton:Undo"])          systemItem = UIBarButtonSystemItemUndo;

    return systemItem;
}

-(void) init:(CDVInvokedUrlCommand*)command
{
    // Dummy function, see initWithWebView
}

// NOTE: Returned object is owned
- (UIBarButtonItem*)makeButtonWithOptions:(const NSDictionary*)options title:(NSString*)title imageName:(NSString*)imageName actionOnSelf:(SEL)actionOnSelf
{
    NSNumber *useImageAsBackgroundOpt = [NSNumber numberWithInt:1];
    float fixedMarginLeft = 13;
    float fixedMarginRight = 13;

    if(options)
    {
        useImageAsBackgroundOpt = [options objectForKey:@"useImageAsBackground"];
        id fixedMarginLeftOpt = [options objectForKey:@"fixedMarginLeft"];
        id fixedMarginRightOpt = [options objectForKey:@"fixedMarginRight"];

        if(fixedMarginLeftOpt && fixedMarginLeftOpt != [NSNull null])
            fixedMarginLeft = [fixedMarginLeftOpt floatValue];
        if(fixedMarginRightOpt && fixedMarginRightOpt != [NSNull null])
            fixedMarginRight = [fixedMarginRightOpt floatValue];
    }

    bool useImageAsBackground = useImageAsBackgroundOpt ? [useImageAsBackgroundOpt boolValue] : false;

    if((title && [title length] > 0) || useImageAsBackground)
    {
        if(useImageAsBackground && imageName && [imageName length] > 0)
        {
            return [self backgroundButtonFromImage:imageName title:title
                                             fixedMarginLeft:fixedMarginLeft fixedMarginRight:fixedMarginRight
                                             target:self action:actionOnSelf];
        }
        else
        {
            return [[UIBarButtonItem alloc] initWithTitle:title style:UIBarButtonItemStylePlain target:self action:actionOnSelf];
        }
    }
    else if (imageName && [imageName length] > 0)
    {
        UIBarButtonSystemItem systemItem = [NavigationBar getUIBarButtonSystemItemForString:imageName];

        if(systemItem != -1)
            return [[UIBarButtonItem alloc] initWithBarButtonSystemItem:systemItem target:self action:actionOnSelf];
        else
            return [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:imageName] style:UIBarButtonItemStylePlain target:self action:actionOnSelf];
    }
    else
    {
        // Fail silently
        NSLog(@"Invalid setup{Left/Right}Button parameters\n");
        return nil;
    }
}

- (void)setupLeftButton:(CDVInvokedUrlCommand*)command
{
    NSString * title = [command.arguments objectAtIndex:0];
    NSString * imageName = [command.arguments objectAtIndex:1];
    const NSDictionary *options = [command.arguments objectAtIndex:2];

    UIBarButtonItem *newButton = [self makeButtonWithOptions:options title:title imageName:imageName actionOnSelf:@selector(leftButtonTapped)];
    navBarController.navItem.leftBarButtonItem = newButton;
    navBarController.leftButton = newButton;
    [newButton release];
}

- (void)setupRightButton:(CDVInvokedUrlCommand*)command
{
    NSString * title = [command.arguments objectAtIndex:0];
    NSString * imageName = [command.arguments objectAtIndex:1];
    const NSDictionary *options = [command.arguments objectAtIndex:2];

    UIBarButtonItem *newButton = [self makeButtonWithOptions:options title:title imageName:imageName actionOnSelf:@selector(rightButtonTapped)];
    navBarController.navItem.rightBarButtonItem = newButton;
    navBarController.rightButton = newButton;
    [newButton release];
}

- (void)hideLeftButton:(CDVInvokedUrlCommand*)command
{
    [[navBarController navItem] setLeftBarButtonItem:nil];
}

- (void)showLeftButton:(CDVInvokedUrlCommand*)command
{
    [[navBarController navItem] setLeftBarButtonItem:[navBarController leftButton]];
}

- (void)hideRightButton:(CDVInvokedUrlCommand*)command
{
    [[navBarController navItem] setRightBarButtonItem:nil];
}

- (void)showRightButton:(CDVInvokedUrlCommand*)command
{
    [[navBarController navItem] setRightBarButtonItem:[navBarController rightButton]];
}

-(void) leftButtonTapped
{
    NSString * jsCallBack = @"window.plugins.navigationBar.leftButtonTapped();";
    [self.webView stringByEvaluatingJavaScriptFromString:jsCallBack];
}

-(void) rightButtonTapped
{
    NSString * jsCallBack = @"window.plugins.navigationBar.rightButtonTapped();";
    [self.webView stringByEvaluatingJavaScriptFromString:jsCallBack];
}

-(void) show:(CDVInvokedUrlCommand*)command
{
    if (!navBar)
        [self create:nil];

    if ([navBar isHidden])
    {
        [navBar setHidden:NO];
        [self correctWebViewBounds];
    }
}


-(void) hide:(CDVInvokedUrlCommand*)command
{
    if (navBar && ![navBar isHidden])
    {
        [navBar setHidden:YES];
        [self correctWebViewBounds];
    }
}

/**
 * Resize the navigation bar (this should be called on orientation change)
 * This is important in playing together with the tab bar plugin, especially because the tab bar can be placed on top
 * or at the bottom, so the navigation bar bounds also need to be changed.
 */
- (void)resize:(CDVInvokedUrlCommand*)command
{
    [self correctWebViewBounds];
}

-(void) setTitle:(CDVInvokedUrlCommand*)command
{
    if(!navBar)
        return;

    NSString  *title = [command.arguments objectAtIndex:0];
    [navBarController navItem].title = title;

    // Reset otherwise overriding logo reference
    [navBarController navItem].titleView = NULL;
}

-(void) setLogo:(CDVInvokedUrlCommand*)command
{
    NSString *logoURL = [command.arguments objectAtIndex:0];
    UIImage *image = nil;

    if (logoURL && logoURL != @"")
    {
        if ([logoURL hasPrefix:@"http://"] || [logoURL hasPrefix:@"https://"])
        {
            NSData * data = [NSData dataWithContentsOfURL:[NSURL URLWithString:logoURL]];
            image = [UIImage imageWithData:data];
        }
        else
            image = [UIImage imageNamed:logoURL];

        if (image)
        {
            UIImageView * view = [[[UIImageView alloc] initWithImage:image] autorelease];
            [view setContentMode:UIViewContentModeScaleAspectFit];
            [view setBounds: CGRectMake(0, 0, 100, 30)];
            [[navBarController navItem] setTitleView:view];
        }
    }
}

@end
