package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.topcoder.hp.idol.ondemand.RestEntites.LanguageIdentification;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnLanguageIdentificationComplete;
import com.topcoder.hp.idol.ondemand.RestUtils.HTTPMethods;
import com.topcoder.hp.idol.ondemand.RestUtils.RestConsts;
import com.topcoder.hp.idol.ondemand.RestUtils.URLHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class LanguageIdentificationTask extends AsyncTask<String, String, LanguageIdentification> {

    private OnLanguageIdentificationComplete _onLanguageIdentificationComplete = null;
    private String _text = null;
    private Context _context = null;

    public LanguageIdentificationTask(Context context, String text, OnLanguageIdentificationComplete onLanguageIdentificationComplete) {

        _context = context;
        _text = text;
        _onLanguageIdentificationComplete = onLanguageIdentificationComplete;

        this.execute();
    }

    private static LanguageIdentification LanguageIdentificationResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("LanguageIdentificationResponseFromJSON");

        LanguageIdentification languageIdentification = null;

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    languageIdentification = new LanguageIdentification(resultObject);
                } catch (Exception e) {
                    Utilities.HandleError("LanguageIdentificationResponseFromJSON - No message found");
                    return null;
                }
            } else {
                Utilities.HandleError("LanguageIdentificationResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return languageIdentification;
    }

    @Override
    protected LanguageIdentification doInBackground(String... args) {

        InputStream inputStream = null;
        LanguageIdentification result = null;

        try {
            URL url = URLHelper.GetLanguageIdentificationURL(_context, _text);

            HttpGet httpRequest = new HttpGet(url.toURI());
            HttpClient httpclient = HTTPMethods.getSSLIgnorateHttpClient(); //new DefaultHttpClient();

            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

            if (response.getStatusLine().toString().contains(RestConsts.RESPONSE_STATUS_OKAY)) {

                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder theStringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    theStringBuilder.append(line + "\n");
                }
                result = LanguageIdentificationResponseFromJSON(theStringBuilder.toString());
            } else {

                Utilities.HandleError("LanguageIdentificationTask Failed to add text to index, status: [" + response.getStatusLine().toString() + "] URL is: [" + url + "]");

                try {
                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder theStringBuilder = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        theStringBuilder.append(line + "\n");
                    }
                    Utilities.WriteLogcat("Response entity: [" + theStringBuilder.toString() + "]");
                } catch (Exception e) {
                }

                return null;
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }

        return result;
    }

    protected void onPostExecute(LanguageIdentification result) {

        try {
            if (_onLanguageIdentificationComplete != null) {
                _onLanguageIdentificationComplete.OnLanguageIdentificationComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
