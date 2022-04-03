//
//  WGPickerView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/6.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "WGPickerView.h"
#import "OMESoft.h"

@interface WGPickerView ()
@property (retain, nonatomic) UILabel *tmpLabel;
@end

@implementation WGPickerView

//- (void)dealloc
//{
//    [_tmpLabel release];
//    [super dealloc];
//}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        if (!_tmpLabel) {
            self.tmpLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 85, 230-40, 44)];
            //        _tmpLabel.text = @"cm";
            _tmpLabel.backgroundColor= [UIColor clearColor];
            _tmpLabel.font = [UIFont boldSystemFontOfSize:20.0f];
            _tmpLabel.textColor = UIColorFromRGB(0x373737);
            _tmpLabel.textAlignment = NSTextAlignmentRight;
            [self addSubview:_tmpLabel];
        }
    }
    return self;
}


// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    [self bringSubviewToFront:_tmpLabel];
}

- (void)showsSelectionTitle:(NSString *)title
{
    _tmpLabel.text = title;
    [self setNeedsDisplay];
}


@end
