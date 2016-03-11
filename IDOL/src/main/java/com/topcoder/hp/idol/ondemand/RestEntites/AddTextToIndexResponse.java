package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddTextToIndexResponse {

    @Expose
    public String index = null;
    @Expose
    public JSONArray references = null;

    public AddTextToIndexResponse(JSONObject addToIndexObject) {
        try {
            index = addToIndexObject.getString("index");
            references = addToIndexObject.getJSONArray("references");

        } catch (Exception e) {
            Utilities.HandleException(e);
        }

    }

    @Override
    public String toString() {
        return index + " " + references.toString();
    }

}
