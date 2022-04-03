//
//  MXPRParamView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/27.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXPRParamView.h"
#import "consDefine.h"

@interface MXPRParamView ()

@property(nonatomic,strong)UIView *backView;

@end

@implementation MXPRParamView

- (instancetype)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        
        _prValue = [[UILabel alloc]init];
        _prValue.text = @"---";
        
    }
    
    return self;
    
}

-(void)layoutSubviews
{
    CGRect rect = self.bounds;
    CGFloat left,top,width,height;
    left = 13;
    top = 0;
    width = CGRectGetWidth(rect)-50;
    height = PARAM_LABEL_ID_H;
    
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(rect), height)];
    self.backView.backgroundColor = UUPrLabelColor;
    [self addSubview:self.backView];
    
    //标明
    _prlabel = [[UILabel alloc]init];
    _prlabel.frame = CGRectMake(left, top, width, height);
    _prlabel.textAlignment = NSTextAlignmentLeft;
    _prlabel.font = [UIFont systemFontOfSize:PARAM_LABEL_ID_FONT];
    _prlabel.textColor = UUWhite;
    _prlabel.text = @"PR";
    [self addSubview:_prlabel];
    //unit
    left = CGRectGetWidth(rect)-50;
    top = 0;
    width = 50;
    height = PARAM_LABEL_ID_H;
    UILabel* unitLabel = [[UILabel alloc]init];
    unitLabel.frame = CGRectMake(left, top, width, height);
    unitLabel.textAlignment = NSTextAlignmentCenter;
    unitLabel.font = [UIFont systemFontOfSize:PARAM_LABEL_ID_FONT];
    unitLabel.textColor = UUWhite;
    unitLabel.text = @"bpm";
    [self addSubview:unitLabel];
    //value
    left = 0;
    top = PARAM_LABEL_ID_H;
    width = CGRectGetWidth(rect)-50;
    height = CGRectGetHeight(rect)-PARAM_LABEL_ID_H;
    _prValue.frame = CGRectMake(left, top, width, height);
    _prValue.textAlignment = NSTextAlignmentCenter;
    _prValue.font = [UIFont systemFontOfSize:60];
    _prValue.textColor = UUWhite;
    [self addSubview:_prValue];
    //heart
    left  = CGRectGetWidth(rect) - 50;
    top   = PARAM_LABEL_ID_H + (CGRectGetHeight(rect)-PARAM_LABEL_ID_H-20)/2;
    width = 20;
    height = 20;
    _heartImage = [[UIImageView alloc]init];
    _heartImage.frame = CGRectMake(left, top, width, height);
    UIImage* image = [UIImage imageNamed:@"heart.png"];
    [_heartImage setImage:image];
    [self addSubview:_heartImage];
}
@end
