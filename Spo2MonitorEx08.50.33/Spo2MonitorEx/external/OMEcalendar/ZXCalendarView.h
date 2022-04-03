//
//  ZXCalendarView.h
//  BLTTest
//
//  Created by Omesoft on 14-2-11.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZXCalendarTilesView.h"
@protocol ZXCalendarViewDelegate;
@protocol ZXCalendarViewDataSource;
@interface ZXCalendarView : UIView <UIScrollViewDelegate>

@property (strong, nonatomic) NSCalendar *calendar;
@property (weak, nonatomic) id <ZXCalendarViewDelegate> delegate;
@property (weak, nonatomic) id <ZXCalendarViewDataSource> dataSource;
@property (nonatomic, assign) CalendarStyle style;
@property (readonly, strong, nonatomic) NSDate *selectedDate;

- (void)updateStateAnimated:(BOOL)animated;
- (void)setSelectedDate:(NSDate *)date animated:(BOOL)animated;
- (void)reloadCalendar;
@end

@protocol ZXCalendarViewDelegate <NSObject>
- (void)calendarView:(ZXCalendarView *)view didSelectedDate:(NSDate *)date;
- (void)calendarView:(ZXCalendarView *)view didChangedNewHeight:(float)height;
@end

@protocol ZXCalendarViewDataSource <NSObject>
@optional
- (NSArray *)calendarView:(ZXCalendarView *)view markFromDate:(NSDate *)startDate toDate:(NSDate *)endDate;
@end