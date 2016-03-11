package com.topcoder.hp.idol.ondemand.SMSMMS;

import com.google.gson.annotations.Expose;

public class MMS {

    @Expose
    private String _id;
    @Expose
    private String _data;
    @Expose
    private String _type;
    public MMS(String id, String data, String type) {
        _id = id;
        _data = data;
        _type = type;
    }

    public String getId() {
        return _id;
    }

    public String getData() {
        return _data;
    }

    public String getType() {
        return _type;
    }

}
