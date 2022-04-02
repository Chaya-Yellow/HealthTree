//
//  DailyCalendarView.m
//  Deputy
//
//  Created by Caesar on 30/10/2014.
//  Copyright (c) 2014 Caesar Li
//
#import "DailyCalendarView.h"
#import "NSDate+CL.h"
#import "UIColor+CL.h"

@interface DailyCalendarView()
@property (nonatomic, strong) UILabel *dateLabel;
@property (nonatomic, strong) UIView *dateLabelContainer;
@property (nonatomic, strong) UILabel* dotLabel;
@end


#define DATE_LABEL_SIZE 32
#define DATE_LABEL_FONT_SIZE 13

@implementation DailyCalendarView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self addSubview:self.dateLabelContainer];
        
        UITapGestureRecognizer *singleFingerTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dailyViewDidClick:)];
        [self addGestureRecognizer:singleFingerTap];
    }
    return self;
}
-(UIView *)dateLabelContainer
{
    if(!_dateLabelContainer){
        float x = (self.bounds.size.width - DATE_LABEL_SIZE)/2;
        _dateLabelContainer = [[UIView alloc] initWithFrame:CGRectMake(x, 0, DATE_LABEL_SIZE, DATE_LABEL_SIZE+3)];
        _dateLabelContainer.backgroundColor = [UIColor clearColor];
        //_dateLabelContainer.layer.cornerRadius = DATE_LABEL_SIZE/2;
        //_dateLabelContainer.clipsToBounds = YES;
        [_dateLabelContainer addSubview:self.dateLabel];
        [_dateLabelContainer addSubview:self.dotLabel];
    }
    return _dateLabelContainer;
}
-(UILabel *)dateLabel
{
    if(!_dateLabel){
        _dateLabel = [[UILabel alloc] initWithFrame:CGRectMake(2, 0,DATE_LABEL_SIZE-4, DATE_LABEL_SIZE-4)];
        _dateLabel.backgroundColor = [UIColor clearColor];
        _dateLabel.textColor = [UIColor whiteColor];
        _dateLabel.layer.cornerRadius =(DATE_LABEL_SIZE-4)/2;
        _dateLabel.clipsToBounds = YES;
        _dateLabel.textAlignment = NSTextAlignmentCenter;
        _dateLabel.font = [UIFont systemFontOfSize:DATE_LABEL_FONT_SIZE];
    }
    
    return _dateLabel;
}

-(UILabel*)dotLabel
{
    if(!_dotLabel)
    {
        _dotLabel = [[UILabel alloc]initWithFrame:CGRectMake((DATE_LABEL_SIZE-4)/2,DATE_LABEL_SIZE-2,4,4)];
        _dotLabel.backgroundColor = [UIColor grayColor];
        _dotLabel.layer.cornerRadius = 2;
        _dotLabel.clipsToBounds = YES;
    }
    return _dotLabel;
}

-(void)setDate:(NSDate *)date
{
    _date = date;
    
    [self setNeedsDisplay];
}
-(void)setBlnSelected: (BOOL)blnSelected
{
    _blnSelected = blnSelected;
    [self setNeedsDisplay];
}
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    self.dateLabel.text = [self.date getDateOfMonth];
    
}

-(void)markSelected:(BOOL)blnSelected
{
    //    DLog(@"mark date selected %@ -- %d",self.date, blnSelected);
    if([self.date isDateToday]){
        self.dateLabel.backgroundColor = (blnSelected)?[UIColor whiteColor]: [UIColor colorWithHex:0x0081c1];
        
        self.dateLabel.textColor = (blnSelected)?[UIColor colorWithHex:0x0081c1]:[UIColor whiteColor];
    }else{
        self.dateLabel.backgroundColor = (blnSelected)?[UIColor whiteColor]: [UIColor clearColor];
        
        self.dateLabel.textColor = (blnSelected)?[UIColor colorWithRed:52.0/255.0 green:161.0/255.0 blue:255.0/255.0 alpha:1.0]:[self colorByDate];
    }
}
-(UIColor *)colorByDate
{
    return [self.date isPastDate]?[UIColor colorWithHex:0x7BD1FF]:[UIColor whiteColor];
}

-(void)dailyViewDidClick: (UIGestureRecognizer *)tap
{
    [self.delegate dailyCalendarViewDidSelect: self.date];
}
@end


// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com 
