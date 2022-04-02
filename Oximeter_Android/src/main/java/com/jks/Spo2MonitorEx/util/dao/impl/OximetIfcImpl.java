package com.jks.Spo2MonitorEx.util.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.jks.Spo2MonitorEx.util.dao.OximetIfc;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.dbhelper.DBHelper;
import com.jks.Spo2MonitorEx.util.dbhelper.DBSetData;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximetTamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public class OximetIfcImpl implements OximetIfc {
    private DBHelper db = null;
    private String table = DBSetData.TABLENAME_Oximet;

    public OximetIfcImpl(Context context) {
        db = DBHelper.getInstance(context, DBSetData.DBNAME);
    }

    @Override
    public List<Oximet> findAll() {
        List<Oximet> list = new ArrayList<Oximet>();
        Cursor cursor = db.findAll(table);
        while (cursor.moveToNext()) {
            Oximet oximet = getOximet(cursor);
            list.add(oximet);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    @Override
    public void insert(Oximet oximet) {
        db.insertOximet(oximet);
    }

    @Override
    public void insert(OximetTamp oximet) {
        db.insertOximet(oximet);
    }

    @Override
    public void update(Oximet oximet) {
        db.updateOximet(oximet);
    }

    @Override
    public void delete(int id) {
        db.deleteByKey(table, "_id", id);
    }

    @Override
    public Oximet findByID(int id) {
        Cursor cursor = db.findByKey(table, "_id", id);
        Oximet oximet = null;
        if (cursor.moveToFirst()) {
            oximet = getOximet(cursor);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return oximet;
    }

    @Override
    public List<Oximet> findOximetsByTime(String date, int familyId) {
        Calendar calendar = MyDateUtil.calendarSetTime(date);
        Cursor cursor = db.findByRecordDateToOximet(table, MyDateUtil.getPreDay(calendar),
                MyDateUtil.getNextDay(calendar), familyId);
        List<Oximet> oximets = new ArrayList<Oximet>();
        while (cursor.moveToNext()) {
            Oximet oximet = getOximet(cursor);
            oximets.add(oximet);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return oximets;
    }

    @Override
    public List<Oximet> findAllOrderByDesc(int familyId) {
        List<Oximet> list = new ArrayList<Oximet>();
        Cursor cursor = db.findAllOrderByDesc(table, familyId);
        while (cursor.moveToNext()) {
            Oximet temp = getOximet(cursor);
            list.add(temp);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    @Override
    public List<Oximet> findAfterNowTime(String date) {
        List<Oximet> list = new ArrayList<Oximet>();
        Cursor cursor = db.findOximetAfterNowTime(table, "RecordDate", date);
        while (cursor.moveToNext()) {
            Oximet temp = getOximet(cursor);
            list.add(temp);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    @Override
    public List<Oximet> findAllByTime(String time) {
        List<Oximet> oximets = new ArrayList<Oximet>();
        Cursor cursor = db.findByTime(table, time);
        while (cursor.moveToNext()) {
            Oximet oximet = getOximet(cursor);
            Log.e("查找出来的血氧数据",""+oximet.toString());
            oximets.add(oximet);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return oximets;
    }

    @Override
    public void insertOrUpdate(List<Oximet> oximets) {
        for (int i = 0; i < oximets.size(); i++) {
            Oximet oximet = oximets.get(i);
            insert(oximet);
        }
    }

    @Override
    public Boolean isHaveOximets(String date, int familyId) {
        Calendar calendar = MyDateUtil.calendarSetTime(date);
        Cursor cursor = db.findByRecordDateToOximet(table, MyDateUtil.getPreDay(calendar),
                MyDateUtil.getNextDay(calendar), familyId);
        boolean have = false;
        if (cursor.getCount() > 1)
            have = true;

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        return have;
    }

    @Override
    public List<String> isHaveOximets(String date, String dateTo, int familyId) {
        List<String> days = new ArrayList<String>();
        Calendar calendar = MyDateUtil.calendarSetTime(date);
        Calendar calendarTo = MyDateUtil.calendarSetTime(dateTo);
        Cursor cursor = db.findByRecordDateToOximetAsCount(table, MyDateUtil.getPreDay(calendar),
                MyDateUtil.getCurrentDay(calendarTo), familyId);
        while (cursor.moveToNext()) {
            days.add(cursor.getString(cursor.getColumnIndexOrThrow("RecordDate")));
        }
        return days;
    }

    @Override
    public void deleteTable() {
        db.deleteTable(table);
    }

    private Oximet getOximet(Cursor cursor) {
        Oximet oximet = new Oximet();
        oximet.setFamilyID(cursor.getInt(cursor.getColumnIndexOrThrow("FamilyID")));
        oximet.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")));
        oximet.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        oximet.setIsDeleted(cursor.getInt(cursor.getColumnIndexOrThrow("IsDeleted")));
        oximet.setPart(cursor.getInt(cursor.getColumnIndexOrThrow("Part")));
        oximet.setRESP(cursor.getFloat(cursor.getColumnIndexOrThrow("RESP")));
        oximet.setRecordDate(cursor.getString(cursor.getColumnIndexOrThrow("RecordDate")));
        oximet.setUpdatedDate(cursor.getString(cursor.getColumnIndexOrThrow("UpdatedDate")));
        oximet.setValueId(cursor.getString(cursor.getColumnIndexOrThrow("TempID")));//每个数据的id号
        oximet.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("timestamp")));
        oximet.setSPO2(cursor.getInt(cursor.getColumnIndexOrThrow("SPO2")));
        oximet.setPR(cursor.getInt(cursor.getColumnIndexOrThrow("PR")));
        oximet.setPI(cursor.getFloat(cursor.getColumnIndexOrThrow("PI")));
        return oximet;

    }
}
