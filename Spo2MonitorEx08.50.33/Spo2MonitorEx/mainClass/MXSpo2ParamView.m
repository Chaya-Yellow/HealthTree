//
//  MXSpo2ParamView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/21.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXSpo2ParamView.h"
#import "consDefine.h"
#import "MXBloodBarView.h"


@interface MXSpo2ParamView()
//@property(strong, nonatomic)MXBloodBarView* bloodBarView;
@property(strong, nonatomic)UIView *backView;
@end
@implementation MXSpo2ParamView
{
    int CbloodValue;
}

- (instancetype)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        
        _paramSpo2Value = [[UILabel alloc]init];
        _paramSpo2Value.text = @"--";
        _paramSpo2Value.textAlignment = NSTextAlignmentCenter;
        _paramSpo2Value.font = [UIFont systemFontOfSize:PARAM_VALUE_FONT_IPHONE];
        _paramSpo2Value.textColor = UUWhite;
        [self addSubview:_paramSpo2Value];

        _paramPIValue = [[UILabel alloc] init];
        _paramPIValue.textAlignment = NSTextAlignmentCenter;
        _paramPIValue.font=[UIFont systemFontOfSize:40];
        _paramPIValue.textColor = UUWhite;
        _paramPIValue.text = @"-.-";
        [self addSubview:_paramPIValue];
                
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
    self.backView.backgroundColor = UUSpo2LabelColor;
    [self addSubview:self.backView];
    //标明
    _paramSpo2ID = [[UILabel alloc]init];
    _paramSpo2ID.frame = CGRectMake(left, top, width, height);
    _paramSpo2ID.textAlignment = NSTextAlignmentLeft;
    _paramSpo2ID.font = [UIFont systemFontOfSize:PARAM_LABEL_ID_FONT];
    _paramSpo2ID.textColor = UUWhite;
    _paramSpo2ID.text = @"SpO2";
    [self addSubview:_paramSpo2ID];
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
    unitLabel.text = @"%";
    [self addSubview:unitLabel];
    //
//    left = 50;
//    top =PARAM_LABEL_ID_H+6;
//    width = PARAM_BLOOD_W;
//    height = CGRectGetHeight(rect)-PARAM_LABEL_ID_H-20;
//    _bloodBarView = [[MXBloodBarView alloc]initWithFrame:CGRectMake(left, top, width, height)];
//    _bloodBarView.clipsToBounds = YES;
//    _bloodBarView.alpha = 0.8;
//    _bloodBarView.backgroundColor = [UIColor clearColor];
//    _bloodBarView.layer.cornerRadius = 6;
//    _bloodBarView.layer.borderColor = UUBloodBarFrontColor.CGColor;
//    _bloodBarView.layer.borderWidth = 0.5;
//    [self addSubview:_bloodBarView];
    //value
    left = 20;
    top = PARAM_LABEL_ID_H+16;
    width = CGRectGetWidth(rect)-left-150;
    height = CGRectGetHeight(rect)-PARAM_LABEL_ID_H;
    _paramSpo2Value.frame = CGRectMake(left, top, width, height);

    //PI
    left =CGRectGetWidth(rect)-150;
    top = PARAM_LABEL_ID_H+40;
    width = 25;
    height = PARAM_LABEL_ID_H+20;
    _paramPIID = [[UILabel alloc]initWithFrame:CGRectMake(left, top, width, height)];
    _paramPIID.textAlignment = NSTextAlignmentLeft;
    _paramPIID.font=[UIFont systemFontOfSize:28];
    _paramPIID.textColor = UUWhite;
    _paramPIID.text = @"PI";
    [self addSubview:_paramPIID];
    //PIUnit
    left =CGRectGetWidth(rect)-55;
    top = PARAM_LABEL_ID_H+40;
    width = 25;
    height = PARAM_LABEL_ID_H+20;
    _paramPIUnit = [[UILabel alloc]initWithFrame:CGRectMake(left, top, width, height)];
    _paramPIUnit.textAlignment = NSTextAlignmentCenter;
    _paramPIUnit.font = [UIFont systemFontOfSize:28];
    _paramPIUnit.textColor = UUWhite;
    _paramPIUnit.text = @"%";
    [self addSubview:_paramPIUnit];
    //PIValue
    left =CGRectGetWidth(rect)-150;
    top = 2*PARAM_LABEL_ID_H+50;
    width = 120;
    height = (CGRectGetHeight(rect)-PARAM_LABEL_ID_H)/2-PARAM_LABEL_ID_H+32;
    _paramPIValue.frame = CGRectMake(left, top, width, height);
    _paramPIValue.textAlignment = NSTextAlignmentCenter;
    _paramPIValue.font=[UIFont systemFontOfSize:40];
    _paramPIValue.textColor = UUWhite;

    //resp
//    left = CGRectGetWidth(rect)-50;
//    top = (CGRectGetHeight(rect)-PARAM_LABEL_ID_H)/2+PARAM_LABEL_ID_H;
//    width = 50;
//    height = (CGRectGetHeight(rect)-PARAM_LABEL_ID_H)/2;
//    _respView = [[MXRespParamView alloc]initWithFrame:CGRectMake(left, top, width, height)];
//    _respView.backgroundColor = UUCEllFrontColor;
//    [self addSubview:_respView];
}

//-(void)drawBloodBar:(int)bloodValue
//{
//    CbloodValue = bloodValue;
//    [_bloodBarView DrawBloodBarValue:bloodValue];
//}

-(void)drawRect:(CGRect)rect
{
}
@end
