//
//  OMEDataServiceError.h
//  
//
//  Created by sincan on 14-10-14.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import <Foundation/Foundation.h>

static NSString * const netWorkValidationErrorDomain = @"GetValidationServiceDataErrorDomain";
static NSString * const GetValidationServiceDataErrorDomain = @"GetValidationServiceDataErrorDomain";

@interface OMEDataServiceError : NSObject

+ (BOOL)isDatsServiceValidWithData:(NSDictionary *)resultDic error:(NSError **)aError;
@end
