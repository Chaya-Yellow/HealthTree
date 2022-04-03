//
//  NSString+Omesoft.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (Omesoft)
- (NSDate *)stringWihtDateFormat:(NSString*)dateFormat;
- (NSDate *)stringWihtDateFormat:(NSString *)dateFormat timeZone:(NSTimeZone *)timezone locale:(NSLocale *)locale;

+ (NSString *)jsonStringWithString:(NSString *)str;
+ (NSString *)jsonStringWithDictionary:(NSDictionary *)dic;
+ (NSString *)jsonStringWithArray:(NSArray *)array;
+ (NSString *)jsonStringWithObject:(id)object;
+ (NSString *)md5:(NSString *)string;
- (NSString *)URLEncodedString;
- (BOOL)isPureInt;
- (BOOL)isPureFloat;
- (BOOL)isPureEnAndNumber;
- (BOOL)isValidateEmail;//是否为有限邮箱
- (NSString *)stringByTrimmingNullCharacters;

+ (NSString *)toBinary:(NSInteger)input;//10进制（数字）转2进制（字符串）
+ (NSString *)byteToString:(NSData *)data;//2进制（data）转16进制（字符串）
+ (NSString *)toHex:(NSInteger)input;//10进制（数字）转16进制（字符串）
- (NSData *)hexStringToByte;//16进制（字符串）转2进制（data）
- (NSArray *)componentsSeparatedByLength:(int)length;//根据长度拆分字符
- (NSString *)transformToBinaryByHex;//16进制（字符串）转2进制（字符串）

- (BOOL)isIncludeChCharacter;//判读是否包含中文字符
- (BOOL)isPureEn;//判断是否为全英文
- (NSString *)hexToAsc2;//16进制转asc2
@end
