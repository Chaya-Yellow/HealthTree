//
//  NSString+Omesoft.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "NSString+Omesoft.h"
#import <CommonCrypto/CommonDigest.h>
@implementation NSString (Omesoft)
#pragma mark - NSDate
- (NSDate *)stringWihtDateFormat:(NSString*)dateFormat
{
    return [self stringWihtDateFormat:dateFormat timeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"] locale:[NSLocale systemLocale]];
}

- (NSDate *)stringWihtDateFormat:(NSString *)dateFormat timeZone:(NSTimeZone *)timezone locale:(NSLocale *)locale
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setTimeZone:timezone];
    [formatter setLocale:locale];
    [formatter setDateFormat:dateFormat];
    NSDate *date = [formatter dateFromString:self];
    return date;
}

#pragma mark - JSON
+ (NSString *)jsonStringWithString:(NSString *)str
{
    return [NSString stringWithFormat:@"\"%@\"",[[str stringByReplacingOccurrencesOfString:@"\n"withString:@"\\n"] stringByReplacingOccurrencesOfString:@"\""withString:@"\\\""]];
    
}

+ (NSString *)jsonStringWithDictionary:(NSDictionary *)dic
{
    NSArray *keys = [dic allKeys];
    NSString *result = @"{";
    NSMutableArray *values = [NSMutableArray array];
    for (int i = 0; i < [keys count];  i++) {
        NSString *key = [keys objectAtIndex:i];
        id ob = [dic  objectForKey:key];
        NSString *value = [self jsonStringWithObject:ob];
        if (value) {
            [values addObject:[NSString stringWithFormat:@"\"%@\":%@",key,value]];
        }
        
    }
    result = [result stringByAppendingFormat:@"%@",[values componentsJoinedByString:@","]];
    result = [result stringByAppendingFormat:@"}"];
    return result;
}

+ (NSString *)jsonStringWithArray:(NSArray *)array
{
    NSMutableArray *values = [NSMutableArray array];
    NSString *result = @"[";
    for (id ob in array) {
        NSString *value = [self jsonStringWithObject:ob];
        if (value) {
            [values addObject:[NSString stringWithFormat:@"%@",value]];
        }
    }
    result = [result stringByAppendingFormat:@"%@",[values componentsJoinedByString:@","]];
    result = [result stringByAppendingFormat:@"]"];
    return result;
}

+ (NSString *)jsonStringWithObject:(id)object
{
    if (!object) {
        return nil;
    }
    if ([object isKindOfClass:[NSString class]]) {
        return [self jsonStringWithString:object];
    }else if([object isKindOfClass:[NSArray class]]){
        return [self jsonStringWithArray:object];
    }else if ([object isKindOfClass:[NSDictionary class]]){
        return [self jsonStringWithDictionary:object];
    }else{
        return object;
    }
}

#pragma mark - URL
- (NSString *)URLEncodedString
{
    CFStringRef strRef = CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                                 (CFStringRef)self,
                                                                 (CFStringRef)@"!$&'()*+,-./:;=?@_~%#[]",
                                                                 NULL,
                                                                 kCFStringEncodingUTF8);
    NSString *encodedString = (__bridge NSString *)strRef;
    CFRelease(strRef);
    return encodedString;
}

#pragma mark - Function
- (BOOL)isPureInt{
    NSScanner* scan = [NSScanner scannerWithString:self];
    int val;
    return [scan scanInt:&val] && [scan isAtEnd];
}
//判断是否为浮点型
- (BOOL)isPureFloat{
    NSScanner* scan = [NSScanner scannerWithString:self];
    float val;
    return [scan scanFloat:&val] && [scan isAtEnd];
}


- (NSString *)stringByTrimmingNullCharacters
{
    if ([[self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] isEqualToString:@"null"]) {
        return nil;
    }else{
        return self;
    }
}

- (BOOL)isValidateEmail
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:self];
}


+ (NSString *)md5:(NSString *)string
{
    const char *cStr = [string UTF8String];
    unsigned char result[16];
    CC_MD5(cStr, (CC_LONG)strlen(cStr), result);
    return [NSString stringWithFormat:@"%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",result[0], result[1], result[2], result[3], result[4], result[5], result[6], result[7],result[8], result[9], result[10], result[11],result[12], result[13], result[14], result[15]];
}


#pragma mark - Hex Binary Ten
+ (NSString *)toBinary:(NSInteger)input
{
    if (input == 1 || input == 0) {
        return [NSString stringWithFormat:@"%d", (int)input];
    }
    else {
        return [NSString stringWithFormat:@"%@%d", [self toBinary:input / 2], (int)input % 2];
    }
}

- (NSString *)transformToBinaryByHex
{
    int value =  [[NSString stringWithFormat:@"%lu",strtoul([self UTF8String], 0, 16)] intValue];
    NSString *result = [NSString toBinary:value];
    while ([result length] != [self length] * 4) {
        result = [NSString stringWithFormat:@"0%@",result];
    }
    return result;
    
}

+ (NSString *)byteToString:(NSData *)data
{
    Byte *bytes = (Byte *)[data bytes];
    NSString *hexStr = @"";
    
    for (int i=0;i<[data length];i++) {
        NSString *newHexStr = [NSString stringWithFormat:@"%x", bytes[i]&0xff];///16进制数
        if ([newHexStr length] == 1) {
            hexStr = [NSString stringWithFormat:@"%@0%@", hexStr, newHexStr];
        } else {
            hexStr = [NSString stringWithFormat:@"%@%@", hexStr, newHexStr];
        }
    }
    return hexStr;
}

+ (NSString *)toHex:(NSInteger)input
{
    if (input < 16) {
        return [NSString toTheHex:input];
    }
    else {
        return [NSString stringWithFormat:@"%@%@", [NSString toHex:input / 16], [NSString toTheHex:(input % 16)]];
    }
}

+ (NSString *)toTheHex:(NSInteger)input
{
    if (input >= 16) {
        return nil;
    }
    NSString *result = nil;
    switch (input) {
        case 10:
            result = @"A";
            break;
        case 11:
            result = @"B";
            break;
        case 12:
            result = @"C";
            break;
        case 13:
            result = @"D";
            break;
        case 14:
            result = @"E";
            break;
        case 15:
            result = @"F";
            break;
        default:
            result = [NSString stringWithFormat:@"%ld",(long)input];
            break;
    }
    return result;
}

- (NSData *)hexStringToByte
{
    NSString *hexString=[[self uppercaseString] stringByReplacingOccurrencesOfString:@" " withString:@""];
    if ([hexString length]%2!=0) {
        return nil;
    }
    Byte tempbyt[1]={0};
    NSMutableData* bytes=[NSMutableData data];
    for(int i=0;i<[hexString length];i++)
    {
        unichar hex_char1 = [hexString characterAtIndex:i]; ////两位16进制数中的第一位(高位*16)
        int int_ch1;
        if(hex_char1 >= '0' && hex_char1 <='9')
            int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
        else if(hex_char1 >= 'A' && hex_char1 <='F')
            int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
        else
            return nil;
        i++;
        
        unichar hex_char2 = [hexString characterAtIndex:i]; ///两位16进制数中的第二位(低位)
        int int_ch2;
        if(hex_char2 >= '0' && hex_char2 <='9')
            int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
        else if(hex_char2 >= 'A' && hex_char2 <='F')
            int_ch2 = hex_char2-55; //// A 的Ascll - 65
        else
            return nil;
        
        tempbyt[0] = int_ch1+int_ch2;  ///将转化后的数放入Byte数组里
        [bytes appendBytes:tempbyt length:1];
    }
    return bytes;
}

- (NSArray *)componentsSeparatedByLength:(int)length
{
    int startX = 0;
    NSMutableArray *result = [[NSMutableArray alloc] init];
    while ([self length] >= startX + length) {
        NSString *aStr = [self substringWithRange:NSMakeRange(startX, length)];
        [result addObject:aStr];
        startX += length;
    }
    return result;
}

- (BOOL)isIncludeChCharacter
{
    for(int i=0; i< [self length];i++){
        int a = [self characterAtIndex:i];
        if( a > 0x4e00 && a < 0x9fff)
            return YES;
    }
    return NO;
}

- (BOOL)isPureEn
{
    NSString *regex =@"[a-z][A-Z]";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    return [predicate evaluateWithObject:self];
}

- (BOOL)isPureEnAndNumber
{
    NSString *regex =@"[a-z][A-Z][0-9]";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    return [predicate evaluateWithObject:self];
}

- (NSString *)hexToAsc2
{
    if (self.length % 2 == 0) {
        NSString *result = @"";
        for (int i = 0; i < self.length / 2; i++) {
            NSString *hexStr = [self substringWithRange:NSMakeRange(i * 2, 2)];
            int value =  [[NSString stringWithFormat:@"%lu",strtoul([hexStr UTF8String], 0, 16)] intValue];
            result = [result stringByAppendingString:[NSString stringWithFormat:@"%c",value]];
        }
        return result;
    }
    return nil;
    //    [self characterAtIndex:0]; asc2
}

@end
