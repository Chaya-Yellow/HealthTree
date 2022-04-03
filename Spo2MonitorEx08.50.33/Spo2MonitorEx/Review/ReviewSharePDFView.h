//
//  ReviewSharePDFView.h
//  Spo2MonitorEx
//
//  Created by loyal on 2020/11/6.
//  Copyright © 2020 kim. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ReviewSharePDFView : UIView

- (instancetype)initWithSpo2yVals:(NSArray *)spo2yVals andPRyValues:(NSArray *)pryValues;

@property(nonatomic,strong)UILabel *timeLb;

@end

NS_ASSUME_NONNULL_END
