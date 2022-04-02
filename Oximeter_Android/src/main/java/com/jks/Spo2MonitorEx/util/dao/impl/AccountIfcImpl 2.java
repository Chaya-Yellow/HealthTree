package com.jks.Spo2MonitorEx.util.dao.impl;

import android.content.Context;
import android.database.Cursor;

import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dbhelper.DBHelper;
import com.jks.Spo2MonitorEx.util.dbhelper.DBSetData;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public class AccountIfcImpl implements AccountIfc {
    private DBHelper db = null;
    private String table = DBSetData.TABLENAME_Account;

    public AccountIfcImpl(Context context) {
        db = DBHelper.getInstance(context, DBSetData.DBNAME);
    }

    @Override
    public List<LoginInfo> findAll() {
        List<LoginInfo> list = new ArrayList<LoginInfo>();
        Cursor cursor = db.findAll(table);
        while (cursor.moveToNext()) {
            LoginInfo oximet = getLoginInfo(cursor);
            list.add(oximet);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    @Override
    public LoginInfo findByAccount(String account) {
        for (LoginInfo info : findAll()) {
            if (info.getAccount().equals(account)) {
                return info;
            }
        }
        return null;
    }

    @Override
    public void insert(LoginInfo loginInfo) {
        db.insertLoginInfo(loginInfo);
    }

    @Override
    public void update(LoginInfo loginInfo) {
        db.updateLoginInfo(loginInfo);
    }

    @Override
    public void deleteByID(int id) {
        db.deleteByKey(table, "_id", id);
    }

    @Override
    public LoginInfo findByID(int id) {
        Cursor cursor = db.findByKey(table, "_id", id);
        LoginInfo loginInfo = null;
        if (cursor.moveToFirst()) {
            loginInfo = getLoginInfo(cursor);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return loginInfo;
    }

    @Override
    public List<LoginInfo> findAllOrderByDesc(int familyId) {
        List<LoginInfo> list = new ArrayList<LoginInfo>();
        Cursor cursor = db.findAllOrderByDesc(table, familyId);
        while (cursor.moveToNext()) {
            LoginInfo temp = getLoginInfo(cursor);
            list.add(temp);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    @Override
    public void insertOrUpdate(List<LoginInfo> loginInfos) {
        for (int i = 0; i < loginInfos.size(); i++) {
            LoginInfo loginInfo = loginInfos.get(i);
            insert(loginInfo);
        }
    }

    @Override
    public void deleteTable() {
        db.deleteTable(table);
    }

    private LoginInfo getLoginInfo(Cursor cursor) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        loginInfo.setAccountId(cursor.getInt(cursor.getColumnIndexOrThrow("accountId")));
        loginInfo.setAccount(cursor.getString(cursor.getColumnIndexOrThrow("account")));
        loginInfo.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
        loginInfo.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow("avatar")));
        loginInfo.setClientKey(cursor.getString(cursor.getColumnIndexOrThrow("clientKey")));
        return loginInfo;

    }
}
