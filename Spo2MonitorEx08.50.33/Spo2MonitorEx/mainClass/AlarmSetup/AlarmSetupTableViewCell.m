//
//  AlarmSetupTableViewCell.m
//  Spo2MonitorEx
//
//  Created by loyal on 2020/2/18.
//  Copyright © 2020 kim. All rights reserved.
//

#import "AlarmSetupTableViewCell.h"
#import "AlarmSetupCellView.h"
#import "TTRangeSlider.h"

@interface AlarmSetupTableViewCell ()<TTRangeSliderDelegate>

@property(nonatomic,strong)AlarmSetupCellView *heightView;
@property(nonatomic,strong)AlarmSetupCellView *lowView;
@property(nonatomic,strong)TTRangeSlider *rangeSlider;
@property(nonatomic,strong)UILabel *leftLb;
@property(nonatomic,strong)UILabel *rightLb;

@end

@implementation AlarmSetupTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        
        self.lowView = [[AlarmSetupCellView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_W, 62)];
        [self.contentView addSubview:self.lowView];
        [self.lowView.swiBtn addTarget:self action:@selector(lowSwiBtnClick) forControlEvents:UIControlEventValueChanged];
        
        UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(20, 62, SCREEN_W-40, 0.5)];
        lineView.backgroundColor = [UIColor colorWithRed:1 green:1 blue:1 alpha:0.3];
        [self.contentView addSubview:lineView];
        
    
        self.heightView = [[AlarmSetupCellView alloc] initWithFrame:CGRectMake(0, 62.5, SCREEN_W, 62)];
        [self.contentView addSubview:self.heightView];
        [self.heightView.swiBtn addTarget:self action:@selector(heightSwiBtnClick) forControlEvents:UIControlEventValueChanged];
        
        UIView *lineView_2 = [[UIView alloc] initWithFrame:CGRectMake(20, 124.5, SCREEN_W-40, 0.5)];
        lineView_2.backgroundColor = [UIColor colorWithRed:1 green:1 blue:1 alpha:0.3];
        [self.contentView addSubview:lineView_2];
        
        CGFloat x = 8;
        self.rangeSlider = [[TTRangeSlider alloc]  initWithFrame:CGRectMake(x, 125, SCREEN_W-2*x, 60)];
        self.rangeSlider.delegate = self;
        self.rangeSlider.handleColor = UUSetupTintColor;
        self.rangeSlider.selectedHandleDiameterMultiplier = 1;
        self.rangeSlider.tintColorBetweenHandles = UUSetupTintColor;
        self.rangeSlider.lineHeight= 10;
        self.rangeSlider.handleDiameter = 19;
//        self.rangeSlider.minLabelFont = SYSTEMFONT(17);
//        self.rangeSlider.maxLabelFont = SYSTEMFONT(17);
//        self.rangeSlider.minLabelColour = self.rangeSlider.maxLabelColour = [UIColor whiteColor];
//        self.rangeSlider.labelPosition = LabelPositionBelow;
        self.rangeSlider.hideLabels = YES;
        self.rangeSlider.tintColor = UUSetupTintBackColor;
        [self.contentView addSubview:self.rangeSlider];
        
        self.leftLb = [[UILabel alloc] init];
        self.leftLb.font = SYSTEMFONT(17);
        self.leftLb.textColor = [UIColor whiteColor];
        self.leftLb.textAlignment = NSTextAlignmentCenter;
        [self.contentView addSubview:self.leftLb];
        [self.leftLb mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.bottom.offset(-12);
            make.left.offset(0);
            make.width.mas_equalTo(43);
            
        }];
        
        self.rightLb = [[UILabel alloc] init];
        self.rightLb.font = SYSTEMFONT(17);
        self.rightLb.textColor = [UIColor whiteColor];
        self.rightLb.textAlignment = NSTextAlignmentCenter;
        [self.contentView addSubview:self.rightLb];
        [self.rightLb mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.bottom.offset(-12);
            make.right.offset(0);
            make.width.mas_equalTo(43);
            
        }];
        
        
    }
    
    return self;
    
}

- (void)setSection:(NSInteger)section {
    
    _section = section;
    
    NSUserDefaults *user = [NSUserDefaults standardUserDefaults];
    
    switch (_section) {
        case 0:{
            
            self.heightView.lb.text = NSLocalizedString(@"SP02Hight",@"");
            self.lowView.lb.text = NSLocalizedString(@"SP02LOW", @"");
            
            NSNumber *number_h = [user objectForKey:@"SP_H"];
            NSNumber *number_l = [user objectForKey:@"SP_L"];
            self.heightView.numLb.text = [NSString stringWithFormat:@"%@",@(number_h.intValue)];
            self.lowView.numLb.text = [NSString stringWithFormat:@"%@",@(number_l.intValue)];
            
            self.rangeSlider.minValue = 50;
            self.rangeSlider.maxValue = 100;
            self.leftLb.text = @"50";
            self.rightLb.text = @"100";

            self.rangeSlider.selectedMinimum = number_l.integerValue;
            self.rangeSlider.selectedMaximum = number_h.integerValue;
            
            NSNumber *swi_h = [user objectForKey:@"SP_H_swi"];
            NSNumber *swi_l = [user objectForKey:@"SP_L_swi"];
            
            self.heightView.swiBtn.on = swi_h.boolValue;
            self.lowView.swiBtn.on = swi_l.boolValue;
            
        }
            break;
            
        case 1:{
            
            self.heightView.lb.text = NSLocalizedString(@"PRHight",@"");
            self.lowView.lb.text = NSLocalizedString(@"PRLOW", @"");
            
            NSNumber *number_h = [user objectForKey:@"PR_H"];
            NSNumber *number_l = [user objectForKey:@"PR_L"];
            self.heightView.numLb.text = [NSString stringWithFormat:@"%@",@(number_h.intValue)];
            self.lowView.numLb.text = [NSString stringWithFormat:@"%@",@(number_l.intValue)];
            
            self.rangeSlider.minValue = 40;
            self.rangeSlider.maxValue = 200;
            self.leftLb.text = @"40";
            self.rightLb.text = @"200";

            self.rangeSlider.selectedMinimum = number_l.integerValue;
            self.rangeSlider.selectedMaximum = number_h.integerValue;
            
            NSNumber *swi_h = [user objectForKey:@"PR_H_swi"];
            NSNumber *swi_l = [user objectForKey:@"PR_L_swi"];
            
            self.heightView.swiBtn.on = swi_h.boolValue;
            self.lowView.swiBtn.on = swi_l.boolValue;
            
        }
            break;
            
        default:{
            
            self.heightView.lb.text = NSLocalizedString(@"PIHight",@"");
            self.lowView.lb.text = NSLocalizedString(@"PILOW", @"");
            
            NSNumber *number_h = [user objectForKey:@"PI_H"];
            NSNumber *number_l = [user objectForKey:@"PI_L"];
            self.heightView.numLb.text = [NSString stringWithFormat:@"%@",@(number_h.intValue)];
            self.lowView.numLb.text = [NSString stringWithFormat:@"%@",@(number_l.intValue)];
            
            self.rangeSlider.minValue = 0;
            self.rangeSlider.maxValue = 20;
            self.leftLb.text = @"0";
            self.rightLb.text = @"20";

            self.rangeSlider.selectedMinimum = number_l.integerValue;
            self.rangeSlider.selectedMaximum = number_h.integerValue;
            
            NSNumber *swi_h = [user objectForKey:@"PI_H_swi"];
            NSNumber *swi_l = [user objectForKey:@"PI_L_swi"];
            
            self.heightView.swiBtn.on = swi_h.boolValue;
            self.lowView.swiBtn.on = swi_l.boolValue;
            
        }
            break;
    }
}

#pragma mark - Action -
- (void)lowSwiBtnClick {
    
    NSString *key = @"";
    switch (self.section) {
        case 0:{
            
            key = @"SP_L_swi";
            
        }
            break;
        
        case 1:{
                 
            key = @"PR_L_swi";
         }
             break;
            
        default:{
            
            key = @"PI_L_swi";
        }
            break;
    }
    
    [[NSUserDefaults standardUserDefaults] setBool:self.lowView.swiBtn.isOn forKey:key];
    
}

- (void)heightSwiBtnClick {
    
    NSString *key = @"";
    
    switch (self.section) {
        case 0:{
            
            key = @"SP_H_swi";
        }
            break;
        
        case 1:{
            
            key = @"PR_H_swi";
                 
         }
             break;
            
        default:{
            
            key = @"PI_H_swi";
            
        }
            break;
    }
    
    [[NSUserDefaults standardUserDefaults] setBool:self.heightView.swiBtn.isOn forKey:key];
    
}

#pragma mark - TTRangeSliderDelegate -

-(void)rangeSlider:(TTRangeSlider *)sender didChangeSelectedMinimumValue:(float)selectedMinimum andMaximumValue:(float)selectedMaximum{
    
    NSLog(@"Custom slider updated. Min Value: %.0f Max Value: %.0f", selectedMinimum, selectedMaximum);
    
    NSUserDefaults *user = [NSUserDefaults standardUserDefaults];
    
    switch (self.section) {
        case 0:{
            
            [user setFloat:selectedMinimum forKey:@"SP_L"];
            [user setFloat:selectedMaximum forKey:@"SP_H"];
            
        }
            break;
            
        case 1:{
           
            [user setFloat:selectedMinimum forKey:@"PR_L"];
            [user setFloat:selectedMaximum forKey:@"PR_H"];
           
        }
            break;
            
        default:{
               
            [user setFloat:selectedMinimum forKey:@"PI_L"];
            [user setFloat:selectedMaximum forKey:@"PI_H"];
               
        }
            break;
    }
    
    self.heightView.numLb.text = [NSString stringWithFormat:@"%.0f",selectedMaximum];
    self.lowView.numLb.text = [NSString stringWithFormat:@"%.0f",selectedMinimum];
    
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
