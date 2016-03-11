package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddTextAttachmentToIndexResponse {

    @Expose
    public JSONArray references = null;
    @Expose
    public String index = null;
    public AddTextAttachmentToIndexResponse(JSONObject jsonObject) {
        try {
            index = jsonObject.getString("index");
            references = jsonObject.getJSONArray("references");
        } catch (Exception e) {
            Utilities.HandleException(e);
        }

    }

    @Override
    public String toString() {
        return index + " " + references.toString();
    }

}
