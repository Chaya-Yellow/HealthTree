//
//  ZXCalendarTilesView.m
//  BLTTest
//
//  Created by Omesoft on 14-2-21.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "ZXCalendarTilesView.h"
#import "OMESoft.h"

@interface ZXCalendarTilesView ()
@property (strong, nonatomic) NSDate *tilesDate;
@property (nonatomic) NSUInteger daysInMoth;
@property (nonatomic , strong) NSArray *marksDate;
@property (nonatomic, assign) CGPoint startPoint;
@property (nonatomic, assign) BOOL isMove;
@property (nonatomic, strong) NSArray *contentDatesForWeek;
@end
@implementation ZXCalendarTilesView
@synthesize selectedImage = _selectedImage;
@synthesize markImage = _markImage;
@synthesize todayImage = todayImage;
@synthesize normalImgae = _normalImgae;
@synthesize delegate = _delegate;
@synthesize style = _style;

#pragma mark - Init




- (id)initWithCoder:(NSCoder *)aDecoder
{
    return [self init];
}

- (id)init
{
    return [self initWithMonthDate:[NSDate date] marks:nil];
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self initMonthDate:[[NSDate date] UTCDate]];
    }
    return self;
}

- (id)initWithMonthDate:(NSDate *)date marks:(NSArray *)marks
{
    self = [super init];
    if (self) {
        [self initMonthDate:date];
        self.marksDate = marks;
        self.style  = CalendarStyleWeek;
        self.selectedDate = [[NSDate date] UTCDate];
        self.isShowSelectedDate = YES;
        self.backgroundColor = [UIColor clearColor];
    }
    return self;
}

- (void)initMonthDate:(NSDate *)date
{
    
    self.tilesDate = date;
    NSDate *nextMothDate = [_tilesDate nextMonth];
    self.daysInMoth = [_tilesDate daysBetweenDate:nextMothDate];
    
    float height = [self viewHeightWithDate:self.tilesDate];
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height);
    _contentDatesForWeek = nil;
    [self setNeedsDisplay];
}

#pragma mark - Private Methods
- (float)viewHeightWithDate:(NSDate *)date
{
    int length;
    if (self.style == CalendarStyleWeek) {
        length = 1;
    } else {
        NSCalendar *calendar = [[NSLocale currentLocale] objectForKey:NSLocaleCalendar];
        NSDate *startOfDate = [date startOfMonth];
        length = [calendar rangeOfUnit:NSWeekCalendarUnit inUnit:NSMonthCalendarUnit forDate:startOfDate].length;
    }
    return CALENDAR_TILE_HEIGHT * length + 12;
}

- (CGRect) rectForCellAtIndex:(int)index
{
	int row = index / 7;
	int col = index % 7;
	float startX = 13;
    float cellWidth = (self.frame.size.width - startX*2) / 7;
	return CGRectMake(startX + col*cellWidth, row*CALENDAR_TILE_HEIGHT+6, cellWidth, CALENDAR_TILE_HEIGHT);
}

- (void)drawTileWithRect:(CGRect)rect date:(NSDate *)date
{
    UIImage *bgImage = nil;
    [[UIColor whiteColor] set];
    NSInteger day = [date dayInDate];
    if ([[date startOfDay] isEqualToDate:[self.selectedDate startOfDay]] && self.isShowSelectedDate) {
        [[UIColor whiteColor] set];
        bgImage = self.selectedImage;
    } else {
        if ([self.marksDate containsObject:[date startOfDay]]) {
            bgImage = self.markImage;
        } else {
            
            if ([[[[NSDate date] UTCDate] startOfDay] isEqualToDate:[date startOfDay]]) {
                bgImage = self.todayImage;
            } else {
                bgImage = self.normalImgae;
            }
        }
    }
    
    if (bgImage) {
        BOOL isMoreWidth = rect.size.width > bgImage.size.width ? YES : NO;
        BOOL isMoreHeight = rect.size.height > bgImage.size.height ? YES : NO;
        float startX = rect.origin.x + (isMoreWidth ? (rect.size.width - bgImage.size.width) / 2.0 : 0);
        float startY = rect.origin.y + (isMoreHeight ? (rect.size.height - bgImage.size.height) / 2.0 : 0);
        CGRect ImageRect =  CGRectMake(startX, startY, isMoreWidth ? bgImage.size.width : rect.size.width, isMoreHeight ? bgImage.size.height : rect.size.height);
        [bgImage drawInRect:ImageRect];
    }
    
    NSString *str = [NSString stringWithFormat:@"%d",day];
    UIFont *strFont = [UIFont systemFontOfSize:13.0f];
    CGSize strSize = [str sizeWithFont:strFont constrainedToSize:CGSizeMake(rect.size.width, rect.size.height)];
    rect.origin.y = rect.origin.y + (rect.size.height - strSize.height) / 2.0;
    rect.size.height = strSize.height;
    [str drawInRect: rect withFont:strFont lineBreakMode: NSLineBreakByWordWrapping
          alignment: NSTextAlignmentCenter];
}

#pragma mark - Draw
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    
    if (self.style == CalendarStyleWeek) {
        for (int k = 0; k < [self.contentDatesForWeek count]; k++) {
            NSDate *currentDate = [self.contentDatesForWeek objectAtIndex:k];
            CGRect lastRect = [self rectForCellAtIndex:k];
            [self drawTileWithRect:lastRect date:currentDate];
        }
    } else {
        NSDate *startOfMothDate = [_tilesDate startOfMonth];
        NSDate *nextMothDate = [startOfMothDate nextMonth];
        NSInteger daysInMonth = [startOfMothDate daysBetweenDate:nextMothDate];
        int weeknum; ////星期日：~星期六：1 ~ 7
        if (![NSLocalizedString(@"language", @"English") isEqualToString:@"zh-Hans"]) {
            weeknum = 1;
        } else {
            weeknum = 2;
        }
        int  weekDayNum = ((int)self.tilesDate.weekday - weeknum + 7) % 7 + 1;
        NSInteger index = weekDayNum - 1;
        for (int i = 1; i <= daysInMonth ; i++) {
            CGRect rect = [self rectForCellAtIndex:index + i - 1];
            NSDate *currentDate = [startOfMothDate changeDayInDate:i];
            [self drawTileWithRect:rect date:currentDate];
        }
    }
    
}

#pragma mark - Touch Events

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    self.startPoint = [touch locationInView:self];
    self.isMove = NO;
    
    if ([_delegate respondsToSelector:@selector(styleWillChanged:)]) {
        [_delegate styleWillChanged:self];
    }
}

/*
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
//    NSLog(@"%f..%f",point.x,point.y);
    self.isMove = YES;
    float offY = point.y - self.startPoint.y;
    BOOL isChanged = NO;
    if (self.style == CalendarStyleWeek) {
        if (offY) {
            isChanged = YES;
        }
        
    } else if (self.style == CalendarStyleMonth) {
        if (!offY) {
            isChanged = YES;
        }
    }
//    if (isChanged) {
//        if ([_delegate respondsToSelector:@selector(view:heightDidChanged:)]) {
//            float height = self.frame.size.height + offY;
//            [_delegate view:self heightDidChanged:height];
//        }
//    }

}
*/
- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    [self touchesEndedWithPoint:point];
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    [self touchesEndedWithPoint:point];
}

- (void)touchesEndedWithPoint:(CGPoint)point
{
    if ([_delegate respondsToSelector:@selector(styleDidCancle:)]) {
        [_delegate styleDidCancle:self];
    }
    if (self.isMove) {
        float offY = point.y - self.startPoint.y;
        if (self.style == CalendarStyleWeek) {
            if (offY > CALENDAR_TILE_HEIGHT) {
//                self.style = CalendarStyleMonth;
//                if ([_delegate respondsToSelector:@selector(styleDidChanged:)]) {
//                    [_delegate styleDidChanged:self];
//                }
            }
        } else {
            if (-offY > CALENDAR_TILE_HEIGHT) {
                self.style = CalendarStyleWeek;
                if ([_delegate respondsToSelector:@selector(styleDidChanged:)]) {
                    [_delegate styleDidChanged:self];
                }
            }
        }
        
    } else {
        CGRect touchRect = CGRectMake(self.startPoint.x - 10, self.startPoint.y - 10, 20, 20);
        if (CGRectContainsPoint(touchRect, point)) {
            float startX = 20;
            float startY = 6;
            if (point.y > (self.bounds.size.height - 6) || point.y < startY || point.x > (self.bounds.size.width - startX) || point.x < startX) {
                return;
            }
            float cellWidth = (self.frame.size.width - 40) / 7;
            int column = (point.x - startX) / cellWidth;
            int row = (point.y - startY) / CALENDAR_TILE_HEIGHT;
            
            
            NSDate *startOfMothDate = [_tilesDate startOfMonth];
            if (self.style == CalendarStyleWeek) {
                NSDate *touchEndDate = [self.contentDatesForWeek objectAtIndex:column];
                if ([self.marksDate containsObject:[touchEndDate startOfDay]] || [[touchEndDate startOfDay] isEqualToDate:[[[NSDate date] UTCDate] startOfDay]]){
                    self.selectedDate = touchEndDate;
                } else {
                    return;
                }
                
            } else {
                NSInteger firWeekday = (int)startOfMothDate.weekday - 1;
                NSInteger day = 1;
                if (row == 0 && column < firWeekday) {
                    return;
                } else {
                    day = column + row  * 7 - (firWeekday - 1);
                }
                
                if (day > self.daysInMoth) {
                    return;
                }
                NSDate *touchEndDate = [startOfMothDate changeDayInDate:day];
                if ([self.marksDate containsObject:[touchEndDate startOfDay]] || [[touchEndDate startOfDay] isEqualToDate:[self.selectedDate startOfDay]]){
                    self.selectedDate = touchEndDate;
                } else {
                    return;
                }
            }
            
            [self setNeedsDisplay];
            if ([_delegate respondsToSelector:@selector(dateDidChanged:)]) {
                [_delegate dateDidChanged:self];
            }
        }
    }
}


#pragma mark - Public Methods

- (void)setMarks:(NSArray *)marks
{
    self.marksDate = marks;
    [self setNeedsDisplay];
}

- (void)setMonthDate:(NSDate *)monthDate showSelectedDate:(BOOL)isShow
{
    [self initMonthDate:monthDate];
    self.isShowSelectedDate = isShow;
    [self setNeedsDisplay];
}
#pragma mark - Set & Get Methods
- (NSDate *)monthDate
{
    return [NSDate date];
}

- (NSArray *)contentDatesForWeek
{
    if (!_contentDatesForWeek) {
        NSDate *startOfMothDate = [_tilesDate startOfMonth];
        NSDate *nextMothDate = [startOfMothDate nextMonth];
        NSInteger daysInMonth = [startOfMothDate daysBetweenDate:nextMothDate];
        int weeknum; ////星期日：~星期六：1 ~ 7
        if (![NSLocalizedString(@"language", @"English") isEqualToString:@"zh-Hans"]) {
            weeknum = 1;
        } else {
            weeknum = 2;
        }
        int  weekDayNum = ((int)self.tilesDate.weekday - weeknum + 7) % 7 + 1;
        NSMutableArray *daysArray = [[NSMutableArray alloc] initWithCapacity:7];
        
        BOOL isPre = (self.tilesDate.dayInDate - weekDayNum) < 0 ? YES : NO;
        BOOL isLast = (self.tilesDate.dayInDate + (7 - weekDayNum)) > daysInMonth ? YES : NO;
        if (isPre || isLast) {
            NSDate *startTileDateInMonth;
            if (isPre) {
                startTileDateInMonth = [self.tilesDate startOfMonth];
            } else {
                startTileDateInMonth = [[self.tilesDate startOfMonth] nextMonth];
            }
            int  startTileDateInMonthweekDayNum = ((int)startTileDateInMonth.weekday - weeknum + 7) % 7 + 1;
            NSDate *preTileDateInMonth = [startTileDateInMonth previousMonth];
            NSInteger preDaysInMonth = [preTileDateInMonth daysBetweenDate:startTileDateInMonth];
            
            NSInteger preStartDay = preDaysInMonth - startTileDateInMonthweekDayNum + 2;
            for (int pre = 0; pre < startTileDateInMonthweekDayNum - 1; pre++) {
                [daysArray addObject:[preTileDateInMonth changeDayInDate:preStartDay + pre]];
            }
            int curtNum = 7 - startTileDateInMonthweekDayNum + 1;
            for (int cur = 1; cur <= curtNum; cur++) {
                [daysArray addObject:[startTileDateInMonth changeDayInDate:cur]];
            }
        } else {
            int curStartDay = self.tilesDate.dayInDate - weekDayNum + 1;
            for (int n =0; n < 7; n++) {
                [daysArray addObject:[startOfMothDate changeDayInDate:curStartDay + n]];
            }
        }
        _contentDatesForWeek = [NSArray arrayWithArray:daysArray];
    }
    return _contentDatesForWeek;
}
- (void)setSelectedDate:(NSDate *)s
{
    _selectedDate = s;
    [self setNeedsDisplay];
}

- (void)setStyle:(CalendarStyle)s
{
    _style = s;
    [self setNeedsDisplay];
}

@end
