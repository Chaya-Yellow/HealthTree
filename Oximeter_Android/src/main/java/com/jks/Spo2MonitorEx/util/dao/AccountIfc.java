package com.jks.Spo2MonitorEx.util.dao;

import com.jks.Spo2MonitorEx.util.entity.LoginInfo;

import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public interface AccountIfc {
    /**
     * 查找所有用户设置的闹铃
     */
    List<LoginInfo> findAll();

    LoginInfo findByAccount(String account);

    /**
     * 插入登录的用户
     */
    void insert(LoginInfo loginInfo);

    /**
     * 更改登录用户
     */
    void update(LoginInfo loginInfo);

    /**
     * 删除登录用户
     */
    void deleteByID(int id);

    /**
     * 根据id查找数据
     */
    LoginInfo findByID(int id);

    /**
     * 逆序找出所有
     * @param id
     * @return
     */
    List<LoginInfo> findAllOrderByDesc(int id);

    void insertOrUpdate(List<LoginInfo> loginInfos);

    void deleteTable();
}
