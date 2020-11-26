package com.example.chofem.store.responses;

import com.google.gson.annotations.SerializedName;

public class ResponseObject {

    @SerializedName("MESSAGE")
    public String MESSAGE;
    @SerializedName("STATUS")
    public boolean STATUS;

    public ResponseObject() {
    }
    public String getMESSAGE() {
        return MESSAGE;
    }
    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public boolean isSTATUS() {
        return STATUS;
    }

    public void setSTATUS(boolean STATUS) {
        this.STATUS = STATUS;
    }
}
