//
//  ReviewSharePDFView.m
//  Spo2MonitorEx
//
//  Created by loyal on 2020/11/6.
//  Copyright © 2020 kim. All rights reserved.
//

#import "ReviewSharePDFView.h"

@implementation ReviewSharePDFView

- (instancetype)initWithSpo2yVals:(NSArray *)spo2yVals andPRyValues:(NSArray *)pryValues {
    
    if (self = [super init]) {
        
        self.backgroundColor = UUBackGroundColor;
        
        UILabel *titleLb = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, SCREEN_W, 40)];
        titleLb.textColor = [UIColor whiteColor];
        titleLb.textAlignment = NSTextAlignmentCenter;
        titleLb.font = [UIFont boldSystemFontOfSize:20];
        titleLb.text = @"Spo2 & Pulse Rate Report";
        [self addSubview:titleLb];
        
        self.timeLb = [[UILabel alloc] initWithFrame:CGRectMake(0,40, SCREEN_W, 20)];
        self.timeLb.textColor = [UIColor whiteColor];
        self.timeLb.textAlignment = NSTextAlignmentCenter;
        self.timeLb.font = SYSTEMFONT(13);
        [self addSubview:self.timeLb];
        
        UIView *backView = [[UIView alloc] initWithFrame:CGRectMake(10, 70, SCREEN_W-20, 90)];
        ViewBorderRadius(backView, 4, 0.5, [UIColor colorWithRed:1 green:1 blue:1 alpha:0.5]);
        [self addSubview:backView];
        CGFloat lb_w = backView.frame.size.width/4.0;
        CGFloat lb_h = 30;
        NSLog(@"values:::%@   %@",spo2yVals,pryValues);
        NSInteger spo2_avg = 0;
        NSInteger spo2_max = 0;
        NSInteger spo2_min = 0;
        if (spo2yVals.count>0) {
            
            NSNumber *avg = [spo2yVals valueForKeyPath:@"@avg.floatValue"];
            spo2_avg = avg.integerValue;

            NSNumber *max =[spo2yVals valueForKeyPath:@"@max.floatValue"];
            spo2_max = max.integerValue;

            NSNumber *min =[spo2yVals valueForKeyPath:@"@min.floatValue"];
            spo2_min = min.integerValue;

        }
        
        NSInteger pr_avg = 0;
        NSInteger pr_max = 0;
        NSInteger pr_min = 0;
        
        if (pryValues.count>0) {
            
            NSNumber *avg = [pryValues valueForKeyPath:@"@avg.floatValue"];
            pr_avg = avg.integerValue;

            NSNumber *max =[pryValues valueForKeyPath:@"@max.floatValue"];
            pr_max = max.integerValue;

            NSNumber *min =[pryValues valueForKeyPath:@"@min.floatValue"];
            pr_min = min.integerValue;
        }
        
        NSArray *titleArray = @[@"",@"Spo2",@"PR"];
        NSArray *maxArray = @[@"Max",[NSString stringWithFormat:@"%@",@(spo2_max)],[NSString stringWithFormat:@"%@",@(pr_max)]];
        NSArray *avgArray = @[@"Avg",[NSString stringWithFormat:@"%@",@(spo2_avg)],[NSString stringWithFormat:@"%@",@(pr_avg)]];
        NSArray *minArray = @[@"Min",[NSString stringWithFormat:@"%@",@(spo2_min)],[NSString stringWithFormat:@"%@",@(pr_min)]];

        for (int i = 0; i < 3; i++) {
            
            UILabel *firstLb = [[UILabel alloc] initWithFrame:CGRectMake(5, i*lb_h, lb_w-5, lb_h)];
            firstLb.font = SYSTEMFONT(13);
            firstLb.textColor = [UIColor whiteColor];
            firstLb.text = titleArray[i];
            [backView addSubview:firstLb];
            
            UILabel *maxLb = [[UILabel alloc] initWithFrame:CGRectMake(lb_w, i*lb_h, lb_w, lb_h)];
            maxLb.textColor = [UIColor whiteColor];
            maxLb.textAlignment = NSTextAlignmentCenter;
            maxLb.font = SYSTEMFONT(13);
            maxLb.text = maxArray[i];
            [backView addSubview:maxLb];
            
            UILabel *avgLb = [[UILabel alloc] initWithFrame:CGRectMake(lb_w*2, i*lb_h, lb_w, lb_h)];
            avgLb.textColor = [UIColor whiteColor];
            avgLb.textAlignment = NSTextAlignmentCenter;
            avgLb.font = SYSTEMFONT(13);
            avgLb.text = avgArray[i];
            [backView addSubview:avgLb];
            
            UILabel *minLb = [[UILabel alloc] initWithFrame:CGRectMake(lb_w*3, i*lb_h, lb_w, lb_h)];
            minLb.textColor = [UIColor whiteColor];
            minLb.textAlignment = NSTextAlignmentCenter;
            minLb.font = SYSTEMFONT(13);
            minLb.text = minArray[i];
            [backView addSubview:minLb];
        }
        
    }
    
    return self;
    
}

@end
