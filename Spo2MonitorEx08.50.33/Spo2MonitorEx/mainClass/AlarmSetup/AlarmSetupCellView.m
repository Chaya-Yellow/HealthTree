//
//  AlarmSetupCellView.m
//  Spo2MonitorEx
//
//  Created by loyal on 2020/2/18.
//  Copyright © 2020 kim. All rights reserved.
//

#import "AlarmSetupCellView.h"

@implementation AlarmSetupCellView

- (instancetype)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        
      self.lb = [[UILabel alloc] init];
      self.lb.font = SYSTEMFONT(15);
      self.lb.textColor = [UIColor whiteColor];
      [self addSubview:self.lb];
       
      self.numLb = [[UILabel alloc] init];
      self.numLb.font = SYSTEMFONT(14);
      self.numLb.textColor = [UIColor whiteColor];
      [self addSubview:self.numLb];
      
      self.swiBtn = [[UISwitch alloc] init];
      self.swiBtn.onTintColor = UUSetupTintColor;
      self.swiBtn.backgroundColor = UUSetupTintBackColor;
      ViewRadius(self.swiBtn,self.swiBtn.frame.size.height/2.0);
      [self addSubview:self.swiBtn];
        
      DefineWeakSelf;
      [self.lb mas_makeConstraints:^(MASConstraintMaker *make) {
          
          make.left.offset(17);
          make.centerY.offset(0);
          
      }];
      
      [self.numLb mas_makeConstraints:^(MASConstraintMaker *make) {
          
          make.left.equalTo(weakSelf.lb.mas_right).offset(15);
          make.centerY.offset(0);
          
      }];
      
      [self.swiBtn mas_makeConstraints:^(MASConstraintMaker *make) {
          
          make.centerY.offset(0);
          make.right.offset(-20);
          
      }];
      
        
    }
    
    return self;
    
}

@end
