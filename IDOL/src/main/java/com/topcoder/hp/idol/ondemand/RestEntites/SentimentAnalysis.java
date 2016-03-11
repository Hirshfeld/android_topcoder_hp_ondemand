package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

public class SentimentAnalysis {

    @Expose
    public JSONArray positive = null;
    @Expose
    public JSONArray negative = null;
    @Expose
    public JSONObject aggregate = null;

    public SentimentAnalysis(JSONArray i_positive, JSONArray i_negative, JSONObject i_aggregate) {
        positive = i_positive;
        negative = i_negative;
        aggregate = i_aggregate;
    }

    @Override
    public String toString() {
        try {
            return "score: [" + aggregate.getString("score") + "], sentiment: [" + aggregate.getString("sentiment") + "]";
        } catch (Exception e) {
            return aggregate.toString();
        }
    }

    public JSONArray toJSONArray() {
        JSONArray jsonArr = new JSONArray();
        try {
            jsonArr.put(aggregate);
            jsonArr.put(positive);
            jsonArr.put(negative);
            return jsonArr;

        } catch (Exception e) {
            return jsonArr;
        }
    }

}
