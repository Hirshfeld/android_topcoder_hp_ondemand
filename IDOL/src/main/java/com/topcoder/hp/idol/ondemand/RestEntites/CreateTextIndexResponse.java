package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;

public class CreateTextIndexResponse {

    @Expose
    public String message = null;
    @Expose
    public String index;

    @Override
    public String toString() {
        return index + " " + message;
    }

}
