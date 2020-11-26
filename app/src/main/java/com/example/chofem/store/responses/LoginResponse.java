package com.example.chofem.store.responses;

public class LoginResponse {
    String MESSAGE,FIRST_NAME,LAST_NAME,EMAIL,LANGUAGE,PROFILE_IMAGE,CATEGORY,LATITUDE,LONGITUDE,LOCATION,STORE_ID,PHONE;
    boolean STATUS;

    public LoginResponse() {
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getSTORE_ID() {
        return STORE_ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getLANGUAGE() {
        return LANGUAGE;
    }

    public void setLANGUAGE(String LANGUAGE) {
        this.LANGUAGE = LANGUAGE;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public boolean isSTATUS() {
        return STATUS;
    }

    public void setSTATUS(boolean STATUS) {
        this.STATUS = STATUS;
    }
}
