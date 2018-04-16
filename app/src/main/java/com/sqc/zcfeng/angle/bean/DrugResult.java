package com.sqc.zcfeng.angle.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class DrugResult {

    /**
     * safeStatus : 2
     * sync : false
     * interactionList : []
     * tabooList : [{"drug":{"drugGuid":"8127","drugCommonId":8127,"drugCommonUuid":"1CD440A6C5","drugCommonName":"阿立哌唑片","indications":null,"hasDetail":true},"description":"对于孕妇，应权衡利弊决定是否服用奥派","crowd":"妊娠期妇女","grade":9}]
     * repetitionList : []
     */

    private int safeStatus;
    private boolean sync;
    private List<Interaction> interactionList;
    private List<TabooListBean> tabooList;
    private List<?> repetitionList;

    public int getSafeStatus() {
        return safeStatus;
    }

    public void setSafeStatus(int safeStatus) {
        this.safeStatus = safeStatus;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public List<Interaction> getInteractionList() {
        return interactionList;
    }

    public void setInteractionList(List<Interaction> interactionList) {
        this.interactionList = interactionList;
    }

    public List<TabooListBean> getTabooList() {
        return tabooList;
    }

    public void setTabooList(List<TabooListBean> tabooList) {
        this.tabooList = tabooList;
    }

    public List<?> getRepetitionList() {
        return repetitionList;
    }

    public void setRepetitionList(List<?> repetitionList) {
        this.repetitionList = repetitionList;
    }

    public static class TabooListBean {
        /**
         * drug : {"drugGuid":"8127","drugCommonId":8127,"drugCommonUuid":"1CD440A6C5","drugCommonName":"阿立哌唑片","indications":null,"hasDetail":true}
         * description : 对于孕妇，应权衡利弊决定是否服用奥派
         * crowd : 妊娠期妇女
         * grade : 9
         */

        private DrugBean drug;
        private String description;
        private String crowd;
        private int grade;

        public DrugBean getDrug() {
            return drug;
        }

        public void setDrug(DrugBean drug) {
            this.drug = drug;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCrowd() {
            return crowd;
        }

        public void setCrowd(String crowd) {
            this.crowd = crowd;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public static class DrugBean {
            /**
             * drugGuid : 8127
             * drugCommonId : 8127
             * drugCommonUuid : 1CD440A6C5
             * drugCommonName : 阿立哌唑片
             * indications : null
             * hasDetail : true
             */

            private String drugGuid;
            private int drugCommonId;
            private String drugCommonUuid;
            private String drugCommonName;
            private Object indications;
            private boolean hasDetail;

            public String getDrugGuid() {
                return drugGuid;
            }

            public void setDrugGuid(String drugGuid) {
                this.drugGuid = drugGuid;
            }

            public int getDrugCommonId() {
                return drugCommonId;
            }

            public void setDrugCommonId(int drugCommonId) {
                this.drugCommonId = drugCommonId;
            }

            public String getDrugCommonUuid() {
                return drugCommonUuid;
            }

            public void setDrugCommonUuid(String drugCommonUuid) {
                this.drugCommonUuid = drugCommonUuid;
            }

            public String getDrugCommonName() {
                return drugCommonName;
            }

            public void setDrugCommonName(String drugCommonName) {
                this.drugCommonName = drugCommonName;
            }

            public Object getIndications() {
                return indications;
            }

            public void setIndications(Object indications) {
                this.indications = indications;
            }

            public boolean isHasDetail() {
                return hasDetail;
            }

            public void setHasDetail(boolean hasDetail) {
                this.hasDetail = hasDetail;
            }
        }
    }
    public class Interaction{

        /**
         * drugList : [{"drugGuid":"8127","drugCommonId":8127,"drugCommonUuid":"1CD440A6C5","drugCommonName":"阿立哌唑片","indications":null,"hasDetail":true},{"drugGuid":"5714","drugCommonId":5714,"drugCommonUuid":"1CD946A5C6","drugCommonName":"盐酸氟西汀分散片","indications":null,"hasDetail":true}]
         * grade : 2
         * description : 盐酸氟西汀分散片可抑制阿立哌唑片代谢，使阿立哌唑片血浆药物浓度升高
         */

        private int grade;
        private String description;
        private List<TabooListBean.DrugBean> drugList;

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<TabooListBean.DrugBean> getDrugList() {
            return drugList;
        }

        public void setDrugList(List<TabooListBean.DrugBean> drugList) {
            this.drugList = drugList;
        }


    }
}
