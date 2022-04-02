package com.jks.Spo2MonitorEx.util.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximetTamp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by apple on 16/7/17.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH = DBSetData.DB_PATH;// 设置数据库的路径
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;

    private DBHelper(Context context, int versionCode, String DB_NAME) {
        super(context, DB_NAME, null, versionCode);
        createDataBase(this, context, DB_NAME);// 创建数据库

    }

    public static DBHelper getInstance(Context context, String DB_NAME) {
        if (dbHelper != null) {
            return dbHelper;
        } else {
            int versionCode = SoftParam.getVersionCode(context);
            dbHelper = new DBHelper(context, versionCode, DB_NAME);
            return dbHelper;
        }
    }

    private static void copydatabase(Context context, String dbName) {
        try {
            // 在src中得到预先保存的数据库,并于流的形式返回
            InputStream myInput = context.getClassLoader().getResourceAsStream(dbName);
            // 用流的形式把数据库文件添加到指定的目录里
            String outFileName = DB_PATH + dbName;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);// 复制
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
        }
    }

    private static void createDataBase(DBHelper dbHelper, Context context, String DB_NAME) {
        boolean dbExist = checkDataBase(DB_NAME);
        if (!dbExist) {// 判断手机中是否存在数据库
            copydatabase(context, DB_NAME);// 如果不存在数据库就把数据库复制到指定的路径
            db = dbHelper.getReadableDatabase();
            if (db != null) {
                createDataBase(dbHelper, context, DB_NAME);
            }
        }
    }

    private static boolean checkDataBase(String DB_NAME) {
        File file = new File(DB_PATH);
        if (!file.exists()) {
            file.mkdirs(); // 如果目录中不存在，创建这个目录
        } else {
            try {
                String myPath = DB_PATH + DB_NAME;
                // 检查数据库是否存在
                db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            } catch (SQLiteException e) {
            }
        }
        return db != null ? true : false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("test", "onUpgrade::");
        if (oldVersion <= 41) {
            db.execSQL("ALTER TABLE " + DBSetData.TABLENAME__MEMBER_FAMILY + " RENAME TO "
                    + DBSetData.TABLENAME__MEMBER_FAMILY_TEMP);
            String DoubleQuotes = "\"";

            // CREATE TABLE DBSetData.TABLENAME__MEMBER_FAMILY (
            // +DoubleQuotes+_id+DoubleQuotes+ integer,
            // +DoubleQuotes+FamilyID+DoubleQuotes+ integer NOT NULL,
            // +DoubleQuotes+MemberID+DoubleQuotes+ integer NOT NULL,
            // +DoubleQuotes+Name+DoubleQuotes+ varchar (50) NOT NULL,
            // +DoubleQuotes+Height+DoubleQuotes+ decimal (10, 2) NOT NULL,
            // +DoubleQuotes+Weight+DoubleQuotes+ decimal (10, 2) NOT NULL,
            // +DoubleQuotes+Birthday+DoubleQuotes+ datetime NOT NULL,
            // +DoubleQuotes+Gender+DoubleQuotes+ bit NOT NULL,
            // +DoubleQuotes+Avatar+DoubleQuotes+ varchar (50),
            // +DoubleQuotes+CreatedDate+DoubleQuotes+ datetime NOT NULL,
            // +DoubleQuotes+UpdatedDate+DoubleQuotes+ datetime NOT NULL,
            // PRIMARY KEY (+DoubleQuotes+_id+DoubleQuotes+ ASC),
            // UNIQUE (+DoubleQuotes+FamilyID+DoubleQuotes+ ASC)
            // )
            String sql = "CREATE TABLE " + DBSetData.TABLENAME__MEMBER_FAMILY + "(" + DoubleQuotes + "_id"
                    + DoubleQuotes + " integer," + DoubleQuotes + "FamilyID" + DoubleQuotes
                    + " integer NOT NULL," + DoubleQuotes + "MemberID" + DoubleQuotes + "integer NOT NULL,"
                    + DoubleQuotes + "Name" + DoubleQuotes + " varchar (50) NOT NULL," + DoubleQuotes
                    + "Height" + DoubleQuotes + " decimal (10, 2) NOT NULL," + DoubleQuotes + "Weight"
                    + DoubleQuotes + " decimal (10, 2) NOT NULL," + DoubleQuotes + "Birthday" + DoubleQuotes
                    + " datetime NOT NULL," + DoubleQuotes + "Gender" + DoubleQuotes + " bit NOT NULL,"
                    + DoubleQuotes + "Avatar" + DoubleQuotes + " varchar (50)," + DoubleQuotes
                    + "CreatedDate" + DoubleQuotes + " datetime NOT NULL," + DoubleQuotes + "UpdatedDate"
                    + DoubleQuotes + " datetime NOT NULL,"
                    + DoubleQuotes + "Phone" + DoubleQuotes + " VARCHAR (50),"
                    + "PRIMARY KEY (" + DoubleQuotes + "_id"
                    + DoubleQuotes + " ASC)," + "UNIQUE (" + DoubleQuotes + "FamilyID" + DoubleQuotes
                    + " ASC))";
//			Log.v("test" , "sql::" + sql);
            db.execSQL(sql);
            String chaString = "INSERT INTO " + DBSetData.TABLENAME__MEMBER_FAMILY + " SELECT * FROM "
                    + DBSetData.TABLENAME__MEMBER_FAMILY_TEMP;
            db.execSQL(chaString);
            db.execSQL("DROP TABLE " + DBSetData.TABLENAME__MEMBER_FAMILY_TEMP);

        } else {

        }
    }

    /**
     * 查询表中所有的数据
     *
     * @param table
     * @return
     */
    public Cursor findAll(String table) {
        String sql = "select * from " + table;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 查询表中所有的数据
     *
     * @param table
     * @return
     */
    public Cursor findAllEvent(String table) {
        String sql = "select * from " + table + " where IsDeleted=0";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor findAllOrderByDesc(String table, int familyId) {
        String sql = "select * from " + table + " where FamilyID=" + familyId + "  order by RecordDate desc";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 根据字段查询该行,倒序
     * @param table
     * @param key
     * @param value
     * @return
     */
    public Cursor findByKey(String table, String key, int value) {
        String sql = "select * from " + table + " where " + key + "=" + value + " order by _id desc ";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor == null) {
            return null;
        } else {
            return cursor;
        }

    }

    /**
     * 根据字段查询该行,倒序
     * @param table
     * @param key
     * @param value
     * @return
     */
    public Cursor findByKey(String table, String key, String value) {
        String sql = "select * from " + table + " where " + key + "='" + value + "'";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;

    }

    /**
     * 查询表的所有数据
     * @param table
     * @return
     */
    public Cursor findByKey(String table) {
        String sql = "select * from " + table;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;

    }

    /**
     * 查找某天的全部temp 某天前一天date1，某天后一天date2
     * findByRecordDateToTemp
     * */
    public Cursor findByRecordDateToOximet(String table, String date1, String date2, int familyId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select  *");
        sql.append(" from ");
        sql.append(table);
        sql.append(" where RecordDate >'");
        sql.append(date1);
        sql.append("' and RecordDate < '");
        sql.append(date2);
        sql.append("' and FamilyID=");
        sql.append(familyId);
        sql.append(" and IsDeleted=");
        sql.append("0");
        sql.append(" order by  RecordDate asc");
        Cursor cursor = db.rawQuery(sql.toString(), null);
        return cursor;

    }

    /**
     * 查找某天的全部temp 某天前一天date1，某天后一天date2
     * */
    public Cursor findByRecordDateToOximetAsCount(String table, String date1, String date2, int familyId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select RecordDate from (");
        sql.append("select  strftime('%Y-%m-%d',RecordDate) as RecordDate");
        sql.append(" from ");
        sql.append(table);
        sql.append(" where RecordDate >'");
        sql.append(date1);
        sql.append("' and RecordDate < '");
        sql.append(date2);
        sql.append("' and FamilyID=");
        sql.append(familyId);
        sql.append(" and IsDeleted=");
        sql.append("0");
        sql.append(" order by  RecordDate asc ) group by RecordDate");
        Cursor cursor = db.rawQuery(sql.toString(), null);
        return cursor;

    }

    /**
     * 根据字段删除改行
     *
     * @param table
     * @param key
     * @param value
     */
    public void deleteByKey(String table, String key, int value) {
        String sql = "delete from " + table + " where " + key + "=" + value;
        db.execSQL(sql);
    }

    /**
     * 根据字段删除改行
     *
     * @param table
     * @param key
     * @param value
     */
    public void deleteByKey(String table, String key, String value) {
        String sql = "delete from " + table + " where " + key + "=" + value;
        db.execSQL(sql);
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    public static void setDb(SQLiteDatabase db) {
        DBHelper.db = db;
    }

    /**
     * 查询数据库中key的最大值
     * */
    public Cursor findMaxkey(String table, String[] key, String start, int UserId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (int i = 0; i < key.length; i++) {
            sql.append("max(" + key[i] + ") As " + key[i]);
            if (i != key.length - 1) {
                sql.append(",");
            }
        }
        sql.append(" from " + table + " where RecordDate > " + "date('" + start + "','-0 day') and UserId = "
                + UserId);
        Cursor cursor = db.rawQuery(sql.toString(), null);
        return cursor;
    }

    public void update(String table, String[] keys, String[] values, int[] values1, float[] values2,
                       String Dkey, int Dvalue) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update " + table + " set ");
        for (int i = 0; i < keys.length; i++) {
            sql.append(keys[i]);
            if (i < values.length) {
                sql.append(" = '" + values[i] + "'");
            } else if (i >= values.length && i < values.length + values1.length) {
                sql.append(" = " + values1[i - values.length]);
            } else {
                sql.append(" = " + values2[i - values.length - values1.length]);
            }
            if (i != keys.length - 1) {
                sql.append(" , ");
            }
        }
        sql.append(" where " + Dkey + " = " + Dvalue);
        db.execSQL(sql.toString());
    }

    public void updateTime(String table, String[] keys, String[] values, int[] values1, float[] values2,
                           String Dkey, int Dvalue) {
        String recordDate = MyDateUtil.getDateFormatToString(null);
        StringBuilder sql = new StringBuilder();
        sql.append(" update " + table + " set ");
        for (int i = 0; i < keys.length; i++) {
            sql.append(keys[i]);
            if (i < values.length) {
                sql.append(" = '" + values[i] + "'");
            } else if (i >= values.length && i < values.length + values1.length) {
                sql.append(" = " + values1[i - values.length]);
            } else {
                sql.append(" = " + values2[i - values.length - values1.length]);
            }
            if (i != keys.length - 1) {
                sql.append(" , ");
            }
        }
        sql.append(", Timestamp = '" + recordDate + "' where " + Dkey + " = " + Dvalue);
        db.execSQL(sql.toString());
    }

    /**
     * 查找某一KEY的所有
     */
    public Cursor find2Key(String table, String[] keys) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (int i = 0; i < keys.length; i++) {
            sql.append(keys[i]);
            if (i != keys.length - 1) {
                sql.append(",");
            }
        }
        sql.append(" from " + table);
        Cursor cursor = db.rawQuery(sql.toString(), null);
        return cursor;
    }

    /**
     * 插入一个家庭成员
     */
    public void insertFamily(Family family) {
        String recordDate = MyDateUtil.getDateFormatToString(null);
        String sql = "insert into " + DBSetData.TABLENAME__MEMBER_FAMILY + " values( null,"
                + family.getFamilyId() + "," + family.getMemberId() + ",'" + family.getName() + "',"
                + family.getHeight() + "," + family.getWeight() + ",'" + family.getBirthday() + "',"
                + family.getGender() + ",'" + family.getAvatar() + "','" + recordDate + "','"
                + recordDate + "','" + family.getPhone() + "'" + ")";
        db.execSQL(sql);
    }

    /**
     * 同步：插入或更新或删除表
     *
     * @param familys
     */
    public void inserOrUpdatetFamily(List<Family> familys, List<Family> familysold) {
        String recordDate = MyDateUtil.getDateFormatToString(null);
        db.beginTransaction(); // 手动设置开始事务
        try {
            // 批量处理操作
            boolean isEx = false;
            for (Family familyold : familysold) {
                isEx = false;
                for (Family family : familys) {
                    if (familyold.getFamilyId() == family.getFamilyId()) {
                        isEx = true;
                    }
                }
                if (!isEx) {
                    String sql = "delete from " + DBSetData.TABLENAME__MEMBER_FAMILY + " where FamilyID="
                            + familyold.getFamilyId();
                    db.execSQL(sql);
                }
            }
            for (Family family : familys) {
                String sql = "insert or replace into "
                        + DBSetData.TABLENAME__MEMBER_FAMILY
                        + "(_id,FamilyID,MemberID,Name,Gender,Birthday,Weight,Height,Avatar,CreatedDate,UpdatedDate,Phone) values( null,"
                        + family.getFamilyId() + "," + family.getMemberId() + ",'" + family.getName() + "',"
                        + family.getGender() + ",'" + family.getBirthday() + "'," + family.getWeight() + ","
                        + family.getHeight() + ",'" + family.getAvatar() + "' ,'" + recordDate + "','"
                        + recordDate + "' ,'" + family.getPhone() + "'" + ")";
                db.execSQL(sql);
            }
            db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        } catch (Exception e) {
        } finally {
            db.endTransaction(); // 处理完成
        }
    }

    public void deleteTable(String table) {
        String sql = "delete from " + table;
        db.execSQL(sql);
    }

    public Cursor findByTime(String table, String time) {
        String sql = "select * from " + table + " where " + " datetime(CreatedDate) >   datetime ( '" + time
                + "') ";
        Log.e("看看这个sql语句",""+sql.toString());
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public void insertOximet(Oximet oximet) {
        String sql = "replace into " + DBSetData.TABLENAME_Oximet + " values( null,'" + oximet.getValueId() + "',"
                + oximet.getFamilyID() + "," + oximet.getPart() + "," + oximet.getRESP() + ",'"
                + oximet.getRecordDate() + "','" + oximet.getCreatedDate() + "','" + oximet.getUpdatedDate()
                + "','" + oximet.getIsDeleted() + "','" + oximet.getTimestamp() + "'," + oximet.getSPO2() + ","
                + oximet.getPR() + "," + oximet.getPI() + ")";
        db.execSQL(sql);
    }

    public void insertOximet(OximetTamp oximet) {
        String sql = "replace into " + DBSetData.TABLENAME_Oximet + " values( null,'" + oximet.getValueId() + "',"
                + oximet.getFamilyID() + "," + oximet.getPart() + "," + oximet.getRESP() + ",'"
                + oximet.getRecordDate() + "','" + oximet.getCreatedDate() + "','" + oximet.getUpdatedDate()
                + "','" + (oximet.getIsDeleted() ? 1 : 0) + "','" + oximet.getTimestamp() + "'," + oximet.getSPO2() + ","
                + oximet.getPR() + "," + oximet.getPI() + ")";
        Log.e("看看这条sql语句是什么??",""+sql.toString());
        db.execSQL(sql);
    }

    public void updateOximet(Oximet oximet) {
        String sql = " update " + DBSetData.TABLENAME_Oximet + " set FamilyID=" + oximet.getFamilyID()
                + ",TempID='" + oximet.getValueId() + "', Part=" + oximet.getPart() + ",RESP=" + oximet.getRESP()
                + ",RecordDate='" + oximet.getRecordDate() + "',CreatedDate='" + oximet.getCreatedDate()
                + "',UpdatedDate='" + oximet.getUpdatedDate() + "',timestamp='" + oximet.getTimestamp()
                + "',SPO2=" + oximet.getSPO2() + ",PR=" + oximet.getPR() + ",PI=" + oximet.getPI()
                + ",where _id=" + oximet.getId() + ";";
        db.execSQL(sql);
    }

    public void insertToTime(String time, String date) {
        String sql = "insert into " + DBSetData.TABLENAME_TIME + " values('" + date + "' ,'" + time + "')";
        db.execSQL(sql);
    }

    public Cursor findToTime() {
        String sql = "SELECT * FROM " + DBSetData.TABLENAME_TIME;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 查找某一KEY的所有
     */
    public Cursor findAllByOneKey(String table, String key, int value) {
        String sql = "select * from " + table + " where " + key + " = " + value;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     *
     * @param table
     * @param Key
     * @param nowTime
     * @return
     */
    public Cursor findOximetAfterNowTime(String table, String Key, String nowTime) {
        String sql = "select * from " + table + " where " + " datetime(RecordDate) >=   datetime ( '"
                + nowTime + "') ";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public void insertLoginInfo(LoginInfo loginInfo) {
        String sql = "replace into " + DBSetData.TABLENAME_Account + " values( null,'" + loginInfo.getAccount() + "','"
                + loginInfo.getPassword() + "','" + loginInfo.getAvatar() + "'," + loginInfo.getAccountId()
                + ",'" + loginInfo.getClientKey() + "')";
        db.execSQL(sql);
    }

    public void updateLoginInfo(LoginInfo loginInfo) {
        String sql = " update " + DBSetData.TABLENAME_Account + " set account='" + loginInfo.getAccount()
                + "',password='" + loginInfo.getPassword() + "',avatar='" + loginInfo.getAvatar()
                + "',accountId=" + loginInfo.getAccountId() + ",clientKey='" + loginInfo.getClientKey() + "'"
                + " where _id=" + loginInfo.getId() ;
        db.execSQL(sql);
    }
}
