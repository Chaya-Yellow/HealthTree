//
//  ZXCalendarView.m
//  BLTTest
//
//  Created by Omesoft on 14-2-11.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "ZXCalendarView.h"
#import "ZXCalendarTilesView.h"
#import "OMESoft.h"

@interface ZXCalendarView () <ZXCalendarTilesViewDelegate>
@property (strong, nonatomic) NSDate *selectedDate;
@property (nonatomic, strong) UIView *columnsTitleView;
@property (nonatomic, strong) UILabel *titleCalendarLabel;
@property (nonatomic, strong) ZXCalendarTilesView *mainTilesView;

@property (nonatomic, strong) UIScrollView *scrollView;
@property (nonatomic, strong) ZXCalendarTilesView *nextView;
@property (nonatomic, strong) ZXCalendarTilesView *perviousView;
@property (nonatomic, strong) ZXCalendarTilesView *currentView;
@property (nonatomic, strong) NSDate *calendarDate;
@end

@implementation ZXCalendarView
@synthesize selectedDate = _selectedDate;
@synthesize calendar = _calendar;
@synthesize delegate = _delegate;
@synthesize dataSource = _dataSource;

#pragma mark - Init

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.style = CalendarStyleWeek;
        self.selectedDate = [[NSDate date] UTCDate];
        
        //显示为年月。依当前这个星期的星期一为标准
        int weeknum;//星期日：~星期六：1 ~ 7
        if (![NSLocalizedString(@"language", @"English") isEqualToString:@"zh-Hans"]) {
            weeknum = 1;
        } else {
            weeknum = 2;
        }
        //NSLog(@"weeknum = %d", weeknum);
        int  weekDayNum = ((int)self.selectedDate.weekday - weeknum + 7) % 7;
        self.calendarDate = [[self.selectedDate startOfDay] previousDays:weekDayNum];
        self.calendar = [[NSLocale currentLocale] objectForKey:NSLocaleCalendar];
        
        [self addSubview:self.columnsTitleView];
        
        UIView *linView_top = [[UIView alloc] initWithFrame:CGRectMake(15, 34, SCREEN_W - 30, 0.5)];
        linView_top.backgroundColor = [UIColor colorWithRed:1 green:1 blue:1 alpha:0.3];
        [self addSubview:linView_top];
        
        [self addSubview:self.titleCalendarLabel];
        
        UIView *linView_bottom = [[UIView alloc] initWithFrame:CGRectMake(15, 59, SCREEN_W - 30, 0.5)];
        linView_bottom.backgroundColor = [UIColor colorWithRed:1 green:1 blue:1 alpha:0.3];
        [self addSubview:linView_bottom];
        
//        UILabel *lineLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 44, 320, 1)];
//        lineLabel.backgroundColor = [UIColor grayColor];
//        [self addSubview:lineLabel];
        
        [self.scrollView addSubview:self.currentView];
        [self.scrollView addSubview:self.nextView];
        [self.scrollView addSubview:self.perviousView];
        
        [self addSubview:self.scrollView];
        [self adjustsTilesSizeToFitHeight];
    }
    return self;
}

- (id)init
{
    return [self initWithFrame:CGRectZero];
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    return [self init];
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
    [self adjustsTilesSizeToFitHeight];
    [self showMarks];
}


#pragma mark - Private Methods


- (void)reloadTitleName
{
    self.titleCalendarLabel.text = [self titleName:self.calendarDate];
}

- (UIImage *)roundImageWithDiameter:(float)px
{
    float scale = [[UIScreen mainScreen] scale];
    CGRect imageRect = CGRectMake(0, 0, px * scale, px * scale);
    
    UIGraphicsBeginImageContext(imageRect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetAllowsAntialiasing(context, YES);
    [[UIColor clearColor] set];
    //    CGContextFillRect(context, rect);
    CGContextFillEllipseInRect(context, imageRect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

- (void)showMarks
{
    if ([_dataSource respondsToSelector:@selector(calendarView:markFromDate:toDate:)]) {
       NSArray *array = [_dataSource calendarView:self markFromDate:[self.calendarDate SystemDate] toDate:[[self.calendarDate laterDays:7] SystemDate]];
        [_currentView setMarks:array];
    }
}

- (void)adjustsTilesSizeToFitHeight
{
    /*
     [UIView beginAnimations:nil context:nil];
     [UIView setAnimationDelegate:self];
     [UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
     [UIView setAnimationDidStopSelector:@selector(animationEnded)];
     [UIView setAnimationDelay:0.1];
     [UIView setAnimationDuration:0.4];
     */
    if (self.scrollView.frame.size.height != _currentView.frame.size.height || self.frame.size.height != _currentView.frame.size.height + 44) {
        self.scrollView.scrollEnabled = NO;
        [UIView animateWithDuration:0.5 animations:^{
            
            CGRect beforeRect = self.scrollView.frame;
            beforeRect.size.height = _currentView.frame.size.height;
            self.scrollView.frame = beforeRect;
            self.scrollView.contentSize = CGSizeMake(self.scrollView.contentSize.width, self.scrollView.frame.size.height);
            if ([_delegate respondsToSelector:@selector(calendarView:didChangedNewHeight:)]) {
                [_delegate calendarView:self didChangedNewHeight:_currentView.frame.size.height + 44];
            }
            
        } completion:^(BOOL finished) {
            self.scrollView.scrollEnabled = YES;
        }];
    }
    
}

- (void)reloadViewFrameAndDate
{
    if (self.style == CalendarStyleWeek) {
        [_currentView setMonthDate:self.calendarDate showSelectedDate:YES];
        [_nextView setMonthDate:[self.calendarDate laterDays:7] showSelectedDate:YES];
        [_perviousView setMonthDate:[self.calendarDate previousDays:7] showSelectedDate:YES];
    } else {
        [_currentView setMonthDate:self.calendarDate showSelectedDate:YES];
        [_nextView setMonthDate:[[self.calendarDate startOfMonth] nextMonth] showSelectedDate:YES];
        [_perviousView setMonthDate:[[self.calendarDate  startOfMonth] previousMonth] showSelectedDate:YES];
    }
    
    _currentView.frame = CGRectMake(CGRectGetWidth(_scrollView.frame), 0, _currentView.frame.size.width, _currentView.frame.size.height);
    _nextView.frame = CGRectMake(CGRectGetWidth(self.scrollView.frame) * 2, 0, CGRectGetWidth(self.scrollView.frame), CGRectGetHeight(_nextView.frame));
    _perviousView.frame = CGRectMake(0, 0, CGRectGetWidth(self.scrollView.frame), CGRectGetHeight(_perviousView.frame));
    _scrollView.contentOffset = CGPointMake(_scrollView.frame.size.width, 0);
}
#pragma mark - Set & Get Methods

- (UIView *)columnsTitleView
{
    if (!_columnsTitleView) {
        CGFloat x = 13;
        CGFloat w = self.frame.size.width - 26;
        float cellWidth = w / 7;
        _columnsTitleView  = [[UIView alloc] initWithFrame:CGRectMake(x, 42, w, 12)];
        for (int i = 0; i < 7; i++) {
            NSString *columnNameStr = [self columnName:i];
            UILabel  *columnLabel = [[UILabel alloc] initWithFrame:CGRectMake(cellWidth * i, 0, cellWidth, 12)];
            columnLabel.backgroundColor = [UIColor clearColor];
            columnLabel.font = [UIFont systemFontOfSize:11.0f];
            columnLabel.textAlignment = NSTextAlignmentCenter;
            columnLabel.textColor = [UIColor whiteColor];
            columnLabel.text = columnNameStr;
            [_columnsTitleView addSubview:columnLabel];
        }
    }
    return _columnsTitleView;
}


- (UILabel *)titleCalendarLabel
{
    if (!_titleCalendarLabel) {
        _titleCalendarLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 14, self.frame.size.width, 15)];
        _titleCalendarLabel.textColor = [UIColor whiteColor];
        _titleCalendarLabel.backgroundColor = [UIColor clearColor];
        _titleCalendarLabel.font = [UIFont systemFontOfSize:14.0f];
        _titleCalendarLabel.textAlignment = NSTextAlignmentCenter;
        _titleCalendarLabel.text = [self titleName:[NSDate date]];
    }
    return _titleCalendarLabel;
}

- (NSString*)columnName:(NSInteger)column
{
    if ([NSLocalizedString(@"language", @"language") isEqualToString:@"zh-Hans"])
    {
        NSArray *array = [NSArray arrayWithObjects:
                          NSLocalizedString(@"Week_Mon", "Mon"),
                          NSLocalizedString(@"Week_Tue", "Tue"),
                          NSLocalizedString(@"Week_Wen", "Wen"),
                          NSLocalizedString(@"Week_Thu", "Thu"),
                          NSLocalizedString(@"Week_Fri", "Fri"),
                          NSLocalizedString(@"Week_Sat", "Sat"),
                          NSLocalizedString(@"Week_Sun", "Sun"),
                            nil];
        return [array objectAtIndex:column];
    }
    else
    {
        NSArray *englishArray = [NSArray arrayWithObjects:
                                 NSLocalizedString(@"Week_Sun", "Sun"),
                                 NSLocalizedString(@"Week_Mon", "Mon"),
                                 NSLocalizedString(@"Week_Tue", "Tue"),
                                 NSLocalizedString(@"Week_Wen", "Wen"),
                                 NSLocalizedString(@"Week_Thu", "Thu"),
                                 NSLocalizedString(@"Week_Fri", "Fri"),
                                 NSLocalizedString(@"Week_Sat", "Sat"),
                                 nil];
        return [englishArray objectAtIndex:column];
    }
    
    //    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
//    dateFormatter.dateFormat = @"ccc";
//    NSLocale *locale;
//    NSArray *languages = [NSLocale preferredLanguages];
//    NSString *currentLanguage = [languages objectAtIndex:0];
//    locale = [[NSLocale alloc]initWithLocaleIdentifier:currentLanguage];
//    dateFormatter.locale = locale;
//    NSDateComponents * dateComponents = [[NSDateComponents alloc] init];
//    int weeknum;//星期日：0 星期一 ~星期六：1 ~ 6
//    //NSLog(@"NSlocalString=%@",NSLocalizedString(@"language", @"language"));
//    if ([NSLocalizedString(@"language", @"language") isEqualToString:@"zh-Hans"]) {
//        weeknum = 1;
//    }
//    else
//    {
//        weeknum = 0;
//    }
//    dateComponents.day =  column + weeknum + self.calendar.firstWeekday - 1;
//    NSCalendar *calendar = [NSCalendar currentCalendar];
//    NSDate * date = [calendar dateFromComponents:dateComponents];
//    return [dateFormatter stringFromDate:date];
}

- (NSString *)titleName:(NSDate *)date
{
    NSArray *languages = [NSLocale preferredLanguages];
    NSString *currentLanguage = [languages objectAtIndex:0];
    NSString *dateFormat;
//    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    NSLocale *locale;
    dateFormat = @"MMM yyyy";
    locale = [[NSLocale alloc]initWithLocaleIdentifier:currentLanguage];
    return [date dateToStringWithDateFormate:dateFormat timeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"] locale:locale];
}


- (UIScrollView *)scrollView
{
    if (!_scrollView) {
        _scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 60, self.frame.size.width, self.frame.size.height)];
        _scrollView.showsHorizontalScrollIndicator = NO;
        _scrollView.showsVerticalScrollIndicator = NO;
        _scrollView.pagingEnabled = YES;
        _scrollView.contentSize = CGSizeMake(CGRectGetWidth(_scrollView.frame) * 3, CGRectGetHeight(_scrollView.frame));
        _scrollView.contentOffset = CGPointMake(CGRectGetWidth(_scrollView.frame), 0);
        _scrollView.delegate = self;
        _scrollView.backgroundColor = [UIColor clearColor];
    }
    return _scrollView;
}

- (ZXCalendarTilesView *)nextView
{
    if (!_nextView) {
        NSDate *date;
        if (self.style == CalendarStyleWeek) {
            date = [self.selectedDate laterDays:7];
        } else {
            date = [[self.selectedDate  startOfMonth] nextMonth];
        }
        _nextView = [[ZXCalendarTilesView alloc] initWithMonthDate:date marks:nil];
        _nextView.frame = CGRectMake(CGRectGetWidth(self.scrollView.frame) * 2, 0, CGRectGetWidth(self.scrollView.frame), CGRectGetHeight(_nextView.frame));
        _nextView.selectedImage = [UIImage imageNamed:@"img_calendar_selected.png"];
        _nextView.markImage = [UIImage imageNamed:@"img_calendar_mark.png"];
        _nextView.todayImage = [UIImage imageNamed:@"img_calendar_today.png"];
        _nextView.delegate = self;
    }
    return _nextView;
}

- (ZXCalendarTilesView *)perviousView
{
    if (!_perviousView) {
        NSDate *date;
        if (self.style == CalendarStyleWeek) {
            date = [self.selectedDate previousDays:7];
        } else {
            date = [[self.selectedDate  startOfMonth] previousMonth];
        }
        _perviousView = [[ZXCalendarTilesView alloc] initWithMonthDate:date marks:nil];
        _perviousView.frame = CGRectMake(0, 0, CGRectGetWidth(self.scrollView.frame), CGRectGetHeight(_perviousView.frame));
        _perviousView.selectedImage = [UIImage imageNamed:@"img_calendar_selected.png"];
        _perviousView.markImage = [UIImage imageNamed:@"img_calendar_mark.png"];
        _perviousView.todayImage = [UIImage imageNamed:@"img_calendar_today.png"];
        _perviousView.delegate = self;
    }
    return _perviousView;
}

- (ZXCalendarTilesView *)currentView
{
    if (!_currentView) {
        NSDate *date = self.selectedDate;
        _currentView = [[ZXCalendarTilesView alloc] initWithMonthDate:self.calendarDate marks:nil];
        _currentView.frame = CGRectMake(CGRectGetWidth(self.scrollView.frame), 0, CGRectGetWidth(self.scrollView.frame), _currentView.frame.size.height);
        _currentView.selectedDate = [date startOfDay];
        _currentView.selectedImage = [UIImage imageNamed:@"img_calendar_selected.png"];
        _currentView.markImage = [UIImage imageNamed:@"img_calendar_mark.png"];
        _currentView.todayImage = [UIImage imageNamed:@"img_calendar_today.png"];
        _currentView.delegate = self;
    }
    return _currentView;
}

#pragma mark - Public Methods
- (void)updateStateAnimated:(BOOL)animated
{
    if (self.style == CalendarStyleWeek) {
//        self.style = CalendarStyleMonth;
    } else {
        self.style = CalendarStyleWeek;
    }
    _currentView.style = self.style;
    _perviousView.style = self.style;
    _nextView.style = self.style;
    [self reloadViewFrameAndDate];
    [self adjustsTilesSizeToFitHeight];
    
}

- (void)setSelectedDate:(NSDate *)date animated:(BOOL)animated
{
    self.selectedDate = date;
    _currentView.selectedDate = self.selectedDate;
    _perviousView.selectedDate = self.selectedDate;
    _nextView.selectedDate = self.selectedDate;
    [self updateStateAnimated:animated];
}

- (void)reloadCalendar
{
    [self showMarks];
}

#pragma mark - ScrollView Delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    float offX = scrollView.contentOffset.x;
    if (offX == scrollView.frame.size.width) {
        return;
    }
    if (offX >= CGRectGetWidth(scrollView.frame) * 2 || offX <= 0) {
        if (offX >= CGRectGetWidth(scrollView.frame) * 2) {
            ZXCalendarTilesView *tmpCurrentView = _nextView;
            self.nextView = self.perviousView;
            self.perviousView = self.currentView;
            self.currentView = tmpCurrentView;
//            if (self.style == CalendarStyleWeek) {
//                self.selectedDate = [self.selectedDate laterDays:7];
//            } else {
//                self.selectedDate = [[self.selectedDate startOfMonth] nextMonth];
//            }
            if (self.style == CalendarStyleWeek) {
                self.calendarDate = [self.calendarDate laterDays:7];
            } else {
                self.calendarDate = [[self.calendarDate startOfMonth] nextMonth];
            }
        }
        
        if (offX <= 0) {
            ZXCalendarTilesView *tmpCurrentView = _perviousView;
            self.perviousView = self.nextView;
            self.nextView = self.currentView;
            self.currentView = tmpCurrentView;
//            if (self.style == CalendarStyleWeek) {
//                self.selectedDate = [self.selectedDate previousDays:7];
//            } else {
//                self.selectedDate = [[self.selectedDate startOfMonth] previousMonth];
//            }
            if (self.style == CalendarStyleWeek) {
                self.calendarDate = [self.calendarDate previousDays:7];
            } else {
                self.calendarDate = [[self.calendarDate startOfMonth] previousMonth];
            }

        }
//         NSLog(@"scrollViewDidScroll");
        [self reloadViewFrameAndDate];
//        [self showMarks];
//        self.currentView.selectedDate = [self.selectedDate startOfDay];
        [self reloadTitleName];
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
//    NSLog(@"scrollViewDidEndDecelerating");
    _currentView.frame = CGRectMake(CGRectGetWidth(scrollView.frame), 0, _currentView.frame.size.width, _currentView.frame.size.height);
    
    scrollView.contentOffset = CGPointMake(CGRectGetWidth(scrollView.frame), 0);
    [self showMarks];
//    [self respondsSelectedDate];
    [self adjustsTilesSizeToFitHeight];

    
}

- (void)respondsSelectedDate
{
    if ([_delegate respondsToSelector:@selector(calendarView:didSelectedDate:)]) {
        [_delegate calendarView:self didSelectedDate:_currentView.selectedDate];
    }
}

#pragma mark - ZXCalendarTilesViewDelegate
- (void)styleDidChanged:(ZXCalendarTilesView *)view
{
    if (view == _currentView) {
        _nextView.style = view.style;
        _perviousView.style = view.style;
        self.style = view.style;
        [self reloadViewFrameAndDate];
        [self adjustsTilesSizeToFitHeight];
    }
}

- (void)styleWillChanged:(ZXCalendarTilesView *)view
{
    if (view == _currentView) {
        _scrollView.scrollEnabled = NO;
    }
}

- (void)styleDidCancle:(ZXCalendarTilesView *)view
{
    if (view == _currentView) {
        _scrollView.scrollEnabled = YES;
    }
}

- (void)dateDidChanged:(ZXCalendarTilesView *)view
{
    if (view == _currentView) {
        self.selectedDate = _currentView.selectedDate;
        _perviousView.selectedDate = self.selectedDate;
        _nextView.selectedDate = self.selectedDate;
//        [self reloadTitleName];
        if ([_delegate respondsToSelector:@selector(calendarView:didSelectedDate:)]) {
            [_delegate calendarView:self didSelectedDate:self.selectedDate];
        }
    }
}

- (void)view:(ZXCalendarTilesView *)view heightDidChanged:(float)height
{

}
@end
