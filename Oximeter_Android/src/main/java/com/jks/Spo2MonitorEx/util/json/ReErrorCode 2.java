package com.jks.Spo2MonitorEx.util.json;

import android.content.Context;

import com.jks.Spo2MonitorEx.R;

/**
 * Created by apple on 16/8/23.
 */
public class ReErrorCode {
    // JSON返回标识ID
    public static final int ERRCODE_0 = 0;
    public static final int ERRCODE_401 = 401;
    public static final int ERRCODE_402 = 402;
    public static final int ERRCODE_403 = 403;
    public static final int ERRCODE_404 = 404;
    public static final int ERRCODE_405 = 405;
    public static final int ERRCODE_406 = 406;
    public static final int ERRCODE_407 = 407;
    public static final int ERRCODE_408 = 408;
    public static final int ERRCODE_409 = 409;
    public static final int ERRCODE_410 = 410;
    public static final int ERRCODE_411 = 411;
    public static final int ERRCODE_412 = 412;
    public static final int ERRCODE_413 = 413;
    public static final int ERRCODE_414 = 414;
    public static final int ERRCODE_415 = 415;
    public static final int ERRCODE_416 = 416;
    public static final int ERRCODE_417 = 417;
    public static final int ERRCODE_418 = 418;
    public static final int ERRCODE_419 = 419;
    public static final int ERRCODE_420 = 420;
    public static final int ERRCODE_421 = 421;
    public static final int ERRCODE_422 = 422;
    public static final int ERRCODE_423 = 423;
    public static final int ERRCODE_424 = 424;
    public static final int ERRCODE_425 = 425;
    public static final int ERRCODE_426 = 426;
    public static final int ERRCODE_427 = 427;
    public static final int ERRCODE_428 = 428;
    public static final int ERRCODE_429 = 429;
    public static final int ERRCODE_430 = 430;
    public static final int ERRCODE_431 = 431;
    public static final int ERRCODE_432 = 432;
    public static final int ERRCODE_433 = 433;
    public static final int ERRCODE_434 = 434;
    public static final int ERRCODE_435 = 435;
    public static final int ERRCODE_436 = 436;
    public static final int ERRCODE_438 = 438;
    public static final int ERRCODE_105 = 105;


    // 自定义标识ID
    public static final int ERRCODE_2000 = 2000;
    public static final int ERRCODE_3000 = 3000;
    public static final int ERRCODE_4001 = 4001;
    public static final int ERRCODE_4002 = 4002;
    public static final int ERRCODE_4003 = 4003;
    public static final int ERRCODE_4004 = 4004;// 取消预约成功
    public static final int ERRCODE_4005 = 4005;// 取消预约失败
    public static final int ERRCODE_5000 = 5000;// 服务器错误
    public static final int ERRCODE_5001 = 5001;// 提问医生，信息为空
    public static final int ERRCODE_5002 = 5002;
    public static final int ERRCODE_5003 = 5003;
    public static final int ERRCODE_5004 = 5004;// 成功获取套餐
    public static final int ERRCODE_5005 = 5005;// 失败获取套餐
    public static final int ERRCODE_5555 = 5555;// 下载医生图片成功
    public static final int ERRCODE_5556 = 5556;// 下载家庭成员头像图片成功
    public static final int ERRCODE_6001 = 6001;// email忘记密码SUCEED
    public static final int ERRCODE_6002 = 6002;// email忘记密码FAIL
    public static final int ERRCODE_6661 = 6661;// 下载病人描述图片成功
    public static final int ERRCODE_6662 = 6662;
    public static final int ERRCODE_6663 = 6663;

    public static String getErrodType(Context context, int errorCode) {
        switch (errorCode) {
            // 自定义标识ID
            case ERRCODE_401:
                return context.getString(R.string.json_error_401);
            case ERRCODE_402:
                return context.getString(R.string.json_error_402);
            case ERRCODE_403:
                return context.getString(R.string.json_error_403);
            case ERRCODE_404:
                return context.getString(R.string.json_error_404);
            case ERRCODE_405:
                return context.getString(R.string.json_error_405);
            case ERRCODE_406:
                return context.getString(R.string.json_error_406);
            case ERRCODE_407:
                return context.getString(R.string.json_error_407);
            case ERRCODE_408:
                return context.getString(R.string.json_error_408);
            case ERRCODE_409:
                return context.getString(R.string.json_error_409);
            case ERRCODE_410:
                return context.getString(R.string.json_error_410);
            case ERRCODE_411:
                return context.getString(R.string.json_error_411);
            case ERRCODE_412:
                return context.getString(R.string.json_error_412);
            case ERRCODE_413:
                return context.getString(R.string.json_error_413);
            case ERRCODE_414:
                return context.getString(R.string.json_error_414);
            case ERRCODE_415:
                return context.getString(R.string.json_error_415);
            case ERRCODE_416:
                return context.getString(R.string.json_error_416);
            case ERRCODE_417:
                return context.getString(R.string.json_error_417);
            case ERRCODE_418:
                return context.getString(R.string.json_error_418);
            case ERRCODE_419:
                return context.getString(R.string.json_error_419);
            case ERRCODE_420:
                return context.getString(R.string.json_error_420);
            case ERRCODE_421:
                return context.getString(R.string.json_error_421);
            case ERRCODE_422:
                return context.getString(R.string.json_error_422);
            case ERRCODE_423:
                return context.getString(R.string.json_error_423);
            case ERRCODE_424:
                return context.getString(R.string.json_error_424);
            case ERRCODE_425:
                return context.getString(R.string.json_error_425);
            case ERRCODE_426:
                return context.getString(R.string.json_error_426);
            case ERRCODE_427:
                return context.getString(R.string.json_error_427);
            case ERRCODE_428:
                return context.getString(R.string.json_error_428);
            case ERRCODE_429:
                return context.getString(R.string.json_error_429);
            case ERRCODE_430:
                return context.getString(R.string.json_error_430);
            case ERRCODE_431:
                return context.getString(R.string.json_error_431);
            case ERRCODE_432:
                return context.getString(R.string.json_error_432);
            case ERRCODE_433:
                return context.getString(R.string.json_error_433);
            case ERRCODE_434:
                return context.getString(R.string.json_error_434);
            case ERRCODE_435:
                return context.getString(R.string.json_error_435);
            case ERRCODE_436:
                return context.getString(R.string.json_error_436);
            case ERRCODE_438:
                return context.getString(R.string.json_error_438);
            case ERRCODE_105:
                return context.getString(R.string.json_error_105);
            default:
                try {
                    int resId = context.getResources().getIdentifier(
                            "json_error_" + errorCode, "string",
                            context.getPackageName());
                    String str = context.getString(resId);
                    if (resId != 0 || str != null)
                        return str;
                    else
                        return context.getString(R.string.json_error_1) + errorCode;
                } catch (Exception e) {
                    return context.getString(R.string.json_error_1) + errorCode;
                }
        }
    }
}
