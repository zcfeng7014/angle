package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Result {

    /**
     * diseaseName : 胸廓出口综合征
     * juniorName : 胸外科
     * juniorId : 21
     * seniorId : 2
     * seniorName : 外科
     * diseaseCode : 788
     */

    private String diseaseName;
    private String juniorName;
    private int juniorId;
    private int seniorId;
    private String seniorName;
    private String diseaseCode;

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getJuniorName() {
        return juniorName;
    }

    public void setJuniorName(String juniorName) {
        this.juniorName = juniorName;
    }

    public int getJuniorId() {
        return juniorId;
    }

    public void setJuniorId(int juniorId) {
        this.juniorId = juniorId;
    }

    public int getSeniorId() {
        return seniorId;
    }

    public void setSeniorId(int seniorId) {
        this.seniorId = seniorId;
    }

    public String getSeniorName() {
        return seniorName;
    }

    public void setSeniorName(String seniorName) {
        this.seniorName = seniorName;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }
}
