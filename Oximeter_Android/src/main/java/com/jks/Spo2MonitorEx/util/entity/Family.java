package com.jks.Spo2MonitorEx.util.entity;

import java.io.Serializable;

/**
 * Created by apple on 16/7/16.
 */
public class Family implements Serializable {
    //1为本应用需要用的字段
    private String avatar;//1
    private int id;
    private int familyId;//用于Oximet数据对象, 与memberId相同(每个家庭成员的id)
    private int memberId;//1 等同AccountId(账号id)
    private String name;//1
    private String firstname;
    private String lastname;
    private int gender;//1 0: 男 male 1: 女 female
    private String birthday;//1
    private float weight;//1 单位均以公斤来存
    private float height;//1 单位均以厘米来存
    private String phone = "";//1 默认为""
    private String createdDate;
    private String updatedDate;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", " +
                "avatar: " + getAvatar() + ", " +
                "familyId: " + getFamilyId() + ", " +
                "memberId: " + getMemberId() + ", " +
                "name: " + getName() + ", " +
                "firstname: " + getFirstname() + ", " +
                "lastname: " + getLastname() + ", " +
                "gender: " + getGender() + ", " +
                "birthday: " + getBirthday() + ", " +
                "weight: " + getWeight() + ", " +
                "height: " + getHeight() + "\n";

    }
}
