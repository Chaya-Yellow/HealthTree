//
//  ZXCalendarTilesView.h
//  BLTTest
//
//  Created by Omesoft on 14-2-21.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import <UIKit/UIKit.h>

#define CALENDAR_TILE_WIDTH 43.2
#define CALENDAR_TILE_HEIGHT 30

typedef enum {
    CalendarStyleWeek,
    CalendarStyleMonth
}CalendarStyle;

@protocol ZXCalendarTilesViewDelegate;
@interface ZXCalendarTilesView : UIView

@property (readonly) NSDate *monthDate;
@property (nonatomic, strong) NSDate *selectedDate;
@property (nonatomic, strong) UIImage *selectedImage;
@property (nonatomic, strong) UIImage *markImage;
@property (nonatomic, strong) UIImage *todayImage;
@property (nonatomic, strong) UIImage *normalImgae;
@property (nonatomic, strong) id <ZXCalendarTilesViewDelegate> delegate;
@property (nonatomic, assign) CalendarStyle style;
@property (nonatomic, assign) BOOL isShowSelectedDate;

- (id)initWithMonthDate:(NSDate *)date marks:(NSArray *)marks;
- (void)setMarks:(NSArray *)marks;
- (void)setMonthDate:(NSDate *)monthDate showSelectedDate:(BOOL)isShow;
@end

@protocol ZXCalendarTilesViewDelegate <NSObject>
- (void)dateDidChanged:(ZXCalendarTilesView *)view;
- (void)view:(ZXCalendarTilesView *)view heightDidChanged:(float)height;
- (void)styleDidChanged:(ZXCalendarTilesView *)view;
- (void)styleWillChanged:(ZXCalendarTilesView *)view;
- (void)styleDidCancle:(ZXCalendarTilesView *)view;
@end