//
//  OMESyncDataService.h
//  
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import "OMEDataService.h"

@interface OMESyncDataService : OMEDataService

- (void)requestOMESyncByMemberId:(NSInteger)idNum Key:(NSString *)key json:(NSString *)json;//数据同步

@end
