//
//  MXRespParamView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/27.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXRespParamView.h"
#import "consDefine.h"

@implementation MXRespParamView
-(void)layoutSubviews
{
    CGRect rect = self.bounds;
    CGFloat left,top,width,height;
    left = 0;
    top = 0;
    width = CGRectGetWidth(rect)/2;
    height = PARAM_LABEL_ID_H;
    //标明
    _respLabel = [[UILabel alloc]init];
    _respLabel.frame = CGRectMake(left, top, width, height);
    _respLabel.textAlignment = NSTextAlignmentLeft;
    //_respLabel.backgroundColor = UURespLabelColor;
    _respLabel.font = [UIFont systemFontOfSize:8];
    _respLabel.textColor = UUWhite;
    _respLabel.text = @"Resp";
    [self addSubview:_respLabel];
    //unit
    left =CGRectGetWidth(rect)/2;
    top = 0;
    width =CGRectGetWidth(rect)/2;
    height = PARAM_LABEL_ID_H;
    UILabel* unitLabel = [[UILabel alloc]init];
    unitLabel.frame = CGRectMake(left, top, width, height);
    unitLabel.textAlignment = NSTextAlignmentCenter;
    //unitLabel.backgroundColor = UURespLabelColor;
    unitLabel.font = [UIFont systemFontOfSize:8];
    unitLabel.textColor = UUWhite;
    unitLabel.text = @"rpm";
    [self addSubview:unitLabel];
    //value
    left = 0;
    top = PARAM_LABEL_ID_H;
    width = CGRectGetWidth(rect);
    height = CGRectGetHeight(rect)-PARAM_LABEL_ID_H;
    _respValue = [[UILabel alloc]init];
    _respValue.frame = CGRectMake(left, top, width, height);
    _respValue.textAlignment = NSTextAlignmentCenter;
    _respValue.font = [UIFont systemFontOfSize:8];
    _respValue.textColor = UUWhite;
    _respValue.text = @"";
    [self addSubview:_respValue];
}
@end
