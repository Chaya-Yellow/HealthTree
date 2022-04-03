//
//  OMEConfigure.h
//  OMEDataServiceTest
//
//  Created by omesoft on 14-10-29.
//  Copyright (c) 2014年 Xin. All rights reserved.
//

#define isSyncTempData 1
#define isSyncBCData 0
#define isSyncBPData 0
//#define isSyncBGData 1
#define isSyncUAData 0
#define isSyncTCHData 0
#define isSyncGLUData 0 //享健康BG

#define dbUserFileName @"temp.db"
#define dbMemberTable @"MX_Member"
#define dbFamilyTable @"MX_Family"

/*体温*/
#if isSyncTempData
    #define dbTempTable @"MX_Temp_Temp"
    #define dbEventTable @"MX_Temp_Event"
    #define dbTempUpdateTmpTable @"MX_Temp_Temp_UpdateTmp"
    #define dbEventUpdateTmpTable @"MX_Temp_Event_UpdateTmp"
#endif

/*人体成分*/
#if isSyncBCData
    #define dbBCTable @"MS_Record_Body"
    #define dbBCUpdateTmpTable @"MS_Record_Body_UpdateTmp"
#endif

/*血压*/
#if isSyncBPData
    #define dbBPTable @"MX_Record_BP"
    #define dbBPUpdateTmpTable @"MX_Record_BP_UpdateTmp"
#endif

/*享健康 血糖*/
#if isSyncGLUData
    #define dbGLUTable @"MX_Record_GLU"
    #define dbGLUUpdateTmpTable @"MX_Record_GLU_UpdateTmp"
#endif

/*胆固醇*/
#if isSyncTCHData
    #define dbTCHTable @"MX_Record_TCH"
    #define dbTCHUpdateTmpTable @"MX_Record_TCH_UpdateTmp"
#endif

/*尿酸*/
#if isSyncUAData
    #define dbUATable @"MX_Record_UA"
    #define dbUAUpdateTmpTable @"MX_Record_UA_UpdateTmp"
#endif


