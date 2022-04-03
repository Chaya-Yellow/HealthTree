//
//  OMEDataService.h
//  OMEDataServiceTest
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol OMEDataServiceDelegate;
@interface OMEDataService : NSObject

@property (assign, nonatomic) NSUInteger tag;
@property (assign, nonatomic) id <OMEDataServiceDelegate> delegate;//重新赋值将取消request！

- (void)post:(NSString *)post key:(NSString *)key;
- (void)cancel;
- (NSString *)ipAdress;
@end

@protocol OMEDataServiceDelegate <NSObject>

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic;
- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error;
@end