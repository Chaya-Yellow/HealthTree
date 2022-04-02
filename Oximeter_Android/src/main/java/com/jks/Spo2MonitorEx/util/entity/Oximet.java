package com.jks.Spo2MonitorEx.util.entity;

/**
 * Created by apple on 16/7/23.
 */
public class Oximet {
    private int id;
    //valueId或者familyID不同时, 才能在数据库中新增数据
    private String valueId;//原来的tempId
    private int familyID;
    private int memberId;
    private int part = 1;
    private float RESP;//呼吸
    private String recordDate;
    private String record_date;
    private String createdDate;
    private String created_date;
    private String updatedDate;
    private String updated_date;
    private int isDeleted = 0;
    private int is_deleted = 0;
    private String timestamp;
    private int SPO2;
    private int PR;//脉率
    private float PI;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public int getFamilyID() {
        return familyID;
    }

    public void setFamilyID(int familyID) {
        this.familyID = familyID;
        this.memberId = familyID;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public float getRESP() {
        return RESP;
    }

    public void setRESP(float RESP) {
        this.RESP = RESP;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.record_date = recordDate;
        this.recordDate = recordDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updated_date = updatedDate;
        this.updatedDate = updatedDate;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.is_deleted = isDeleted;
        this.isDeleted = isDeleted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSPO2() {
        return SPO2;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public int getPR() {
        return PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    public float getPI() {
        return PI;
    }

    public void setPI(float PI) {
        this.PI = PI;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
        this.familyID = memberId;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.recordDate = record_date;
        this.record_date = record_date;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.createdDate = created_date;
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updatedDate = updated_date;
        this.updated_date = updated_date;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.isDeleted = is_deleted;
        this.is_deleted = is_deleted;
    }

    @Override
    public String toString() {
        return "Oximet [id=" + id + ", valueId=" + valueId + ", familyID=" + familyID + ", memberId=" + memberId +  ", part=" + part + ", SPO2=" + SPO2
                + ", PR=" + PR + ", PI=" + PI + ", RESP=" + RESP + ", recordDate=" + recordDate + ", createdDate=" + createdDate
                + ", updatedDate=" + updatedDate + ", isDeleted=" + isDeleted + ", timestamp=" + timestamp + "]";
    }
}
