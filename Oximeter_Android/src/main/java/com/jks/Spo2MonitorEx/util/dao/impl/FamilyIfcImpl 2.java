package com.jks.Spo2MonitorEx.util.dao.impl;

import android.content.Context;
import android.database.Cursor;

import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dbhelper.DBHelper;
import com.jks.Spo2MonitorEx.util.dbhelper.DBSetData;
import com.jks.Spo2MonitorEx.util.entity.Family;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public class FamilyIfcImpl implements FamilyIfc {
    DBHelper db = null;
    String table = DBSetData.TABLENAME__MEMBER_FAMILY;
    Context context;

    public FamilyIfcImpl(Context context) {
        db = DBHelper.getInstance(context, DBSetData.DBNAME);
        this.context = context;
    }

    @Override
    public List<Family> findAll() {
        List<Family> familys = new ArrayList<Family>();
        Cursor cursor = db.findAll(table);
        while (cursor.moveToNext()) {
            Family family = getFamily(cursor);
            familys.add(family);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return familys;
    }

    @Override
    public Family findAllByFamilyID(int ID) {
        Cursor cursor = db.findByKey(table, "FamilyID", ID);
        cursor.moveToNext();
        Family family = getFamily(cursor);
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return family;
    }

    @Override
    public void insert(List<Family> familys) {
        for (Family family : familys) {
            db.insertFamily(family);
        }
    }

    @Override
    public void insert(Family family) {
        db.insertFamily(family);
    }

    @Override
    public void deleteByID(int ID) {
        db.deleteByKey(table, "_id", ID);
    }

    @Override
    public void updateByID(Family family) {
        db.update(table, new String[] { "Birthday", "Name", "Avatar", "Phone", "Gender", "Height", "Weight" },
                new String[] { family.getBirthday(), family.getName(), family.getAvatar(), family.getPhone()},
                new int[] { family.getGender() }, new float[] { family.getHeight(), family.getWeight() },
                "_id", family.getId());
    }

    @Override
    public List<Family> findAllName() {
        List<Family> familys = new ArrayList<Family>();
        Cursor cursor = db.find2Key(table, new String[] { "Name", "FamilyID" });
        while (cursor.moveToNext()) {
            Family family = new Family();
            family.setFamilyId(cursor.getInt(cursor.getColumnIndexOrThrow("FamilyID")));
            family.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
            familys.add(family);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return familys;
    }

    @Override
    public void deleteTable() {
        db.deleteTable(table);
    }

    @Override
    public void insertOrUpdate(List<Family> familys) {
        db.inserOrUpdatetFamily(familys, findAll());
    }

    private Family getFamily(Cursor cursor) {
        Family family = new Family();
//		Log.v("test", "cursor::" + cursor.toString());
        family.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        family.setFamilyId(cursor.getInt(cursor.getColumnIndexOrThrow("FamilyID")));
        family.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow("Birthday")));
        family.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate")));
        family.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("Gender")));
        family.setHeight(cursor.getFloat(cursor.getColumnIndexOrThrow("Height")));

        family.setMemberId(cursor.getInt(cursor.getColumnIndexOrThrow("MemberID")));
        family.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
        family.setUpdatedDate(cursor.getString(cursor.getColumnIndexOrThrow("UpdatedDate")));
        family.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow("Weight")));
        family.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow("Avatar")));
        family.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("Phone")));
        return family;
    }
}
