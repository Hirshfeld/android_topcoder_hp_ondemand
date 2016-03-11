package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class LanguageIdentification {

    @Expose
    public String language = null;
    @Expose
    public String language_iso639_2b = null;
    @Expose
    public String encoding = null;
    @Expose
    public JSONArray unicode_scripts = null;
    @Expose
    public JSONObject additional_metadata = null;

    public LanguageIdentification(JSONObject languageIdentificationObject) {

        try {
            language = languageIdentificationObject.getString("language");
            language_iso639_2b = languageIdentificationObject.getString("language_iso639_2b");
            encoding = languageIdentificationObject.getString("encoding");
            unicode_scripts = languageIdentificationObject.getJSONArray("unicode_scripts");
            additional_metadata = languageIdentificationObject.getJSONObject("additional_metadata");
        } catch (Exception e) {
            Utilities.HandleException(e);
        }

    }

    @Override
    public String toString() {
        return language + " " + encoding;
    }

}
