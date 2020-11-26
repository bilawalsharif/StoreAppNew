package com.example.chofem.store.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetStoreProductResponse implements Serializable {
    @SerializedName("MESSAGE")
@Expose
private String mESSAGE;
    @SerializedName("STATUS")
    @Expose
    private Boolean sTATUS;
    @SerializedName("PRODUCT")
    @Expose
    private List<PRODUCT> pRODUCT = null;

    public String getMESSAGE() {
        return mESSAGE;
    }

    public void setMESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public Boolean getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(Boolean sTATUS) {
        this.sTATUS = sTATUS;
    }

    public List<PRODUCT> getPRODUCT() {
        return pRODUCT;
    }

    public void setPRODUCT(List<PRODUCT> pRODUCT) {
        this.pRODUCT = pRODUCT;
    }

    public static class PRODUCT implements Serializable {

        @SerializedName("P_id")
        @Expose
        private String pId;
        @SerializedName("P_name")
        @Expose
        private String pName;
        @SerializedName("P_description")
        @Expose
        private String pDescription;
        @SerializedName("P_price")
        @Expose
        private String pPrice;
        @SerializedName("P_status")
        @Expose
        private String pStatus;
        @SerializedName("P_category")
        @Expose
        private String pCategory;
        @SerializedName("Primary_image")
        @Expose
        private String primaryImage;
        @SerializedName("SA_id")
        @Expose
        private String sAId;
        @SerializedName("P_pic1")
        @Expose
        private String pPic1;
        @SerializedName("P_pic2")
        @Expose
        private String pPic2;

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public String getPDescription() {
            return pDescription;
        }

        public void setPDescription(String pDescription) {
            this.pDescription = pDescription;
        }

        public String getPPrice() {
            return pPrice;
        }

        public void setPPrice(String pPrice) {
            this.pPrice = pPrice;
        }

        public String getPStatus() {
            return pStatus;
        }

        public void setPStatus(String pStatus) {
            this.pStatus = pStatus;
        }

        public String getPCategory() {
            return pCategory;
        }

        public void setPCategory(String pCategory) {
            this.pCategory = pCategory;
        }

        public String getPrimaryImage() {
            return primaryImage;
        }

        public void setPrimaryImage(String primaryImage) {
            this.primaryImage = primaryImage;
        }

        public String getSAId() {
            return sAId;
        }

        public void setSAId(String sAId) {
            this.sAId = sAId;
        }

        public String getPPic1() {
            return pPic1;
        }

        public void setPPic1(String pPic1) {
            this.pPic1 = pPic1;
        }

        public String getPPic2() {
            return pPic2;
        }

        public void setPPic2(String pPic2) {
            this.pPic2 = pPic2;
        }

    }
}
