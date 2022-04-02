//
//  OMESyncDataService.m
//
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import "OMESyncDataService.h"

@implementation OMESyncDataService

#pragma mark - Public Methods

- (void)requestOMESyncByMemberId:(NSInteger)idNum Key:(NSString *)key json:(NSString *)json
{
    [self post:json key:@"SyncData"];
}
@end
