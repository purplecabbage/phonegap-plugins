//
//  PHAnnotation.h
//  PhoneGapLib
//
//  Created by Brett Rudd on 17/03/2010.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface PGAnnotation : NSObject <MKAnnotation> {
@private
    CLLocationCoordinate2D _coordinate;
    NSString *_title;
	NSString *_subTitle;
	NSString *_imageURL;
	NSInteger _index;
	MKPlacemark *_placemark;
	NSString *pinColor;
	bool selected;
}

@property (nonatomic, retain) NSString *title;
@property (nonatomic, retain) NSString *subTitle;
@property (nonatomic, retain) NSString *imageURL;
@property (nonatomic) NSInteger index;
@property (nonatomic, retain) MKPlacemark *placemark;
@property (nonatomic) CLLocationCoordinate2D coordinate;
@property (nonatomic, retain) NSString *pinColor;
@property (nonatomic, assign) bool selected;

- (void)notifyCalloutInfo:(MKPlacemark *)placemark;
- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate index:(NSInteger)index title:(NSString*)title subTitle:(NSString*)subTitle imageURL:(NSString*)imageURL;

@end

