//
//  AlarmSetupHeaderView.m
//  Spo2MonitorEx
//
//  Created by loyal on 2020/2/17.
//  Copyright © 2020 kim. All rights reserved.
//

#import "AlarmSetupHeaderView.h"

@implementation AlarmSetupHeaderView


- (instancetype)initWithReuseIdentifier:(NSString *)reuseIdentifier {
    
    if (self = [super initWithReuseIdentifier:reuseIdentifier]) {
        
        self.backgroundView = ({
        UIView * view = [[UIView alloc] initWithFrame:self.bounds];
        view.backgroundColor = [UIColor colorWithRed:81/255.0 green:84/255.0 blue:120/255.0 alpha:1];
        view;
        });
        
        self.titleLb = [[UILabel alloc] initWithFrame:CGRectMake(21,0, SCREEN_W - 30, 31)];
        self.titleLb.font = [UIFont systemFontOfSize:15];
        self.titleLb.textColor = [UIColor colorWithRed:254/255.0 green:254/255.0 blue:254/255.0 alpha:1];
        [self addSubview:self.titleLb];
        
    }
    
    return self;
}

@end
