package com.jks.Spo2MonitorEx.util.dbhelper;

/**
 * Created by apple on 16/7/17.
 */
public class DBSetData {
    /**
     * 数据库路径
     */
    public final static String DB_PATH = android.os.Environment.getDataDirectory().getAbsolutePath()
            + "/data/com.jks.Spo2MonitorEx/databases/";
    /**
     * 数据库名称
     */
    public final static String DBNAME = "Oximeter.db";
    // Medix_Member_Preipheral
    /**
     * 家庭成员表
     */
    public final static String TABLENAME__MEMBER_FAMILY = "BT_Family";

    /**
     * 家庭成员表 临时表 用于改变数据库结构。
     */
    public final static String TABLENAME__MEMBER_FAMILY_TEMP = "BT_Family_temp";

    /**
     * ------------------------ 时间表
     */
    public final static String TABLENAME_TIME = "BT_TIME";
    /**
     * 设备表
     */
    public final static String TABLENAME_MEDIX_MEMBER_PREIPHERAL = "BT_Preipheral";
    /**
     * 提醒（闹铃）表
     */
    public final static String TABLENAME_PUB_SYNC_REMIND = "BT_Remind";

    public final static String TABLENAME_BT_EVENT = "BT_Event";

    public final static String TABLENAME_Oximet = "BT_Oximet";

    public final static String TABLENAME_Account = "BT_Account";

    /**
     * 知识库的表
     */
    public final static String TABLENAME_KNOWLEDGE = "Class";
    /**
     * 知识库的内容
     */
    public final static String TABLENAME_KNOWLEDGE_CONTENT = "Content";
}
