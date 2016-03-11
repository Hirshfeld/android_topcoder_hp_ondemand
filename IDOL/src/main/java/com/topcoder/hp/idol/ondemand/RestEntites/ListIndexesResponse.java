package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ListIndexesResponse {

    @Expose
    public String message = null;
    @Expose
    public String index;

    public List<Index> indexes = new ArrayList<Index>();

    /*


        */
    @Override
    public String toString() {
        return index + " " + message;
    }

}
