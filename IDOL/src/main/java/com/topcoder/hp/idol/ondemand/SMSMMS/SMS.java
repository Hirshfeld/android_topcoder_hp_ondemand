package com.topcoder.hp.idol.ondemand.SMSMMS;

import com.google.gson.annotations.Expose;

public class SMS {

    private String id;
    private String address;
    private String readState; //"0" for have not read sms and "1" for have read sms
    private String time;
    private String folderName;
    @Expose
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String i_id) {
        id = i_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String i_address) {
        address = i_address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String i_msg) {
        msg = i_msg;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String i_readState) {
        readState = i_readState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String i_time) {
        time = i_time;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String i_folderName) {
        folderName = i_folderName;
    }
}
