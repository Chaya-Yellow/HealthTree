//
//  OMEDataService.m
//  OMEDataServiceTest
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "OMEDataService.h"
#import "AFNetworking.h"
#import "OMEDataServiceError.h"
#import "JSONKit.h"

@interface OMEDataService ()
@property (strong, nonatomic) AFHTTPRequestOperation *operation;
@end

@implementation OMEDataService
@synthesize delegate = _delegate;
@synthesize tag;

#define BLT_SOFT_SERVER_IP_ENGLISH @"http://47.88.25.86:8888/api/data"
#define BLT_SOFT_COM_ENGLISH @"47.88.25.86:8888"

- (void)dealloc
{
    [_operation cancel];
    _operation = nil;
}

#pragma mark - Public Methods
- (void)cancel
{
    [_operation cancel];
}

- (NSString *)ipAdress
{
    /*!
     *    @author luteng, 16-04-13 15:04:00
     *
     *    @brief 更改流量访问时服务器无法响应，未知错误
     */
    //return @"9999999-00000";
    return @"";
    /*
    NSError *error;
    NSURL *ipURL = [NSURL URLWithString:@"http://pv.sohu.com/cityjson?ie=utf-8"];
    NSString *jsonStr = [NSString stringWithContentsOfURL:ipURL encoding:1 error:&error];
    if (!jsonStr) {
        return [error localizedDescription];
    }
    NSRange lrange = [jsonStr rangeOfString:@"{"];
    NSRange rRange = [jsonStr rangeOfString:@"}"];
    jsonStr = [jsonStr substringWithRange:NSMakeRange(lrange.location, rRange.location-lrange.location+1)];
    NSDictionary *ipDic = [NSJSONSerialization JSONObjectWithData:[jsonStr dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
    NSString *result = [ipDic objectForKey:@"cip"];
    NSLog(@" file = %s ,, reslutIpaddress = %@",__FILE__, result);
    return result;
     */
}

#pragma mark - Common Methods
- (void)post:(NSString *)post key:(NSString *)key
{
    [self cancel];
    SEL selector = @selector(requestOMEURLWithPost:key:);
    NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:[self methodSignatureForSelector:selector]];
    invocation.target = self;
    invocation.selector = selector;
    [invocation setArgument:&post atIndex:2];
    [invocation setArgument:&key atIndex:3];
    [invocation retainArguments];
    [invocation performSelector:@selector(invoke) withObject:nil afterDelay:0];
}

- (void)requestOMEURLWithPost:(NSString *)aPostStr key:(NSString *)key
{
    NSString *postString =aPostStr;
    //NSLog(@"%@", postString);
    //NSLog(@"11112222333333");
    NSMutableURLRequest *request;
    request= [NSMutableURLRequest requestWithURL:[NSURL URLWithString:BLT_SOFT_SERVER_IP_ENGLISH]];
    [request addValue:BLT_SOFT_COM_ENGLISH forHTTPHeaderField:@"Host"];
    [request addValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request addValue:[NSString stringWithFormat:@"%ld",(unsigned long)[postString length]] forHTTPHeaderField:@"Content-Length"];
    //[request addValue:[NSString stringWithFormat:@"http://medix.org/%@",key] forHTTPHeaderField:@"SOAPAction"];
    [request setHTTPMethod:@"POST"];
    [request setTimeoutInterval:15];
    [request setHTTPBody:[postString dataUsingEncoding:NSUTF8StringEncoding]];
    self.operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    __block  id <OMEDataServiceDelegate> aDelegate = self.delegate;
    __block OMEDataService *class = self;
    [_operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        //NSMutableString* dic = responseObject;
        __block NSString *responseString = [operation responseString];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            NSMutableDictionary* resultDic=[responseString objectFromJSONString];
            //NSLog(@"responseString = %@, dic = %@", responseString,resultDic);
            NSError* error = nil;
            [OMEDataServiceError isDatsServiceValidWithData:resultDic error:&error];
            dispatch_async(dispatch_get_main_queue(), ^{
                if(!error)
                {
                    //NSLog(@"---***********-------");
                    if([aDelegate respondsToSelector:@selector(dataService:didFinishWithData:)])
                    {
                        [aDelegate dataService:class didFinishWithData:resultDic];
                    }
                }
                else
                {
                    //NSLog(@"&&&&&&&&&&&&&&&&&&&&&");
                    if ([aDelegate respondsToSelector:@selector(dataService:didFailWithError:)]){
                        [aDelegate dataService:class didFailWithError:error];
                    }
                }
            });
        });
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        //NSLog(@"-----------------------------error----------------");
        if (operation.isCancelled) {
            NSLog(@"has been Cancelled!");
        } else {
            if ([aDelegate respondsToSelector:@selector(dataService:didFailWithError:)]){
                //NSLog(@">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>%@", error.localizedDescription);
                [aDelegate dataService:class didFailWithError:error];
            }
        }
    }];
    [_operation start];
}

- (void)setDelegate:(id<OMEDataServiceDelegate>)d
{
    _delegate = d;
    if (!_delegate) {
        [_operation cancel];
    }
}
@end
