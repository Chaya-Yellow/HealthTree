package com.jks.Spo2MonitorEx.util.dao;

import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximetTamp;

import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public interface OximetIfc {
    /**
     * 血氧设置数据库！！！
     *
     * @return
     */

    /**
     * 查找所有用户设置的闹铃
     */
    List<Oximet> findAll();

    /**
     * 插入血氧数据
     */
    void insert(Oximet oximet);

    /**
     * 插入血氧数据
     */
    void insert(OximetTamp oximet);

    /**
     * 更改血氧设置
     */
    void update(Oximet oximet);

    /**
     * 删除血氧设置
     */
    void delete(int id);

    /**
     * 根据id查找数据
     */
    Oximet findByID(int id);

    /**
     * 查找一分钟内血氧的平均值
     *
     * @param date
     * @return
     */

    List<Oximet> findOximetsByTime(String date, int familyId);

    List<Oximet> findAllOrderByDesc(int familyId);

    List<Oximet> findAfterNowTime(String date);

    List<Oximet> findAllByTime(String time);

    public void insertOrUpdate(List<Oximet> oximets);

    public Boolean isHaveOximets(String date, int familyId);

    public List<String> isHaveOximets(String date, String dateTo, int familyId);

    public void deleteTable();
}
