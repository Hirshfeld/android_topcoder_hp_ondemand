package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;

public class Index {

    @Expose
    public String index = null;
    @Expose
    public String type = null;

    @Expose
    public String num_components = null;
    @Expose
    public String date_created = null;
    @Expose
    public String flavor = null;
    @Expose
    public String description = null;

    public Index(String i_index, String i_type) {
        index = i_index;
        type = i_type;
    }

    @Override
    public String toString() {
        return index + " " + type;
    }

}
