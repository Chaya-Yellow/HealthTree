//
//  MXSpo2ParamView.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/21.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MXRespParamView.h"
@interface MXSpo2ParamView : UIView
//SPO2
@property(strong, nonatomic) UILabel* paramSpo2ID;
@property(strong, nonatomic) UILabel* paramSpo2Value;
//PR
@property(strong, nonatomic) UILabel* paramPRID;
@property(strong, nonatomic) UILabel* paramPRValue;
//PI
@property(strong, nonatomic) UILabel* paramPIID;
@property(strong, nonatomic) UILabel* paramPIUnit;
@property(strong, nonatomic) UILabel* paramPIValue;
//Resp
//@property(strong, nonatomic) UILabel* paramRespID;
//@property(strong, nonatomic) UILabel* paramRespValue;
//@property(strong, nonatomic) MXRespParamView* respView;
//灌注图
//-(void)drawBloodBar:(int)bloodValue;
@end
