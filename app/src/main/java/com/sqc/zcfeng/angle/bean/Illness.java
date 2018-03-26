package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2018/3/26.
 */

public class Illness {

    /**
     * id : 55bf03b5c5479fa8da1ea1ce
     * summary : 系有各种病原引起的上呼吸道的急性感染，俗称“感冒”，是小儿最常见的疾病。该病主要侵犯鼻、鼻咽、和咽部。
     * typeName : 中医科
     * subTypeName : 中医儿科
     * name : 小儿感冒
     * subTypeId : 5004
     * typeId : 50
     */

    private String id;
    private String summary;
    private String typeName;
    private String subTypeName;
    private String name;
    private String subTypeId;
    private String typeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(String subTypeId) {
        this.subTypeId = subTypeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
