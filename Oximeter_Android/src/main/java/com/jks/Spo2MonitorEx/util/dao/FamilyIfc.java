package com.jks.Spo2MonitorEx.util.dao;

import com.jks.Spo2MonitorEx.util.entity.Family;

import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public interface FamilyIfc {
    /**
     * 查询所有
     */
    List<Family> findAll();
    /**
     * 根据查某个家庭成员的所有值
     */
    Family findAllByFamilyID(int ID);
    /**
     * 增加一系列家庭成员
     */
    void insert(List<Family> familys);
    /**
     * 增加某家庭成员
     */
    void insert(Family family);
    /**
     * 根据家庭成员id删除该家庭成员
     */
    void deleteByID(int ID);
    /**
     * 更改该家庭成员的信息
     */
    void updateByID(Family family);
    /**
     * 查询家庭成员的名字
     */
    List<Family> findAllName();
    /**
     * 删除表中所有的内容
     */
    void deleteTable();
    /**
     * 添加或更新数据库
     */
    void insertOrUpdate(List<Family> familys);
}
