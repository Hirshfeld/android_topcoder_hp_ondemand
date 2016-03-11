package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.topcoder.hp.idol.ondemand.RestEntites.CreateTextIndexResponse;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnCreateTextIndexComplete;
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
import java.util.concurrent.Executors;

public class CreateTextIndexTask extends AsyncTask<String, String, CreateTextIndexResponse> {

    private OnCreateTextIndexComplete _onCreateTextIndexComplete = null;
    private String _indexName = null;
    private String _indexDescription = null;
    private Context _context = null;

    public CreateTextIndexTask(Context context, String indexName, String indexDescription, OnCreateTextIndexComplete onCreateTextIndexComplete) {

        _context = context;
        _indexName = indexName;
        _indexDescription = indexDescription;
        _onCreateTextIndexComplete = onCreateTextIndexComplete;

        this.executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private static CreateTextIndexResponse CreateIndexResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("CreateIndexResponseFromJSON");

        CreateTextIndexResponse createTextIndexResponse = new CreateTextIndexResponse();

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    createTextIndexResponse = new Gson().fromJson(resultObject.toString(), CreateTextIndexResponse.class);
                } catch (Exception e) {
                    Utilities.HandleError("CreateIndexResponseFromJSON - No message found");
                    return null;
                }
            } else {
                Utilities.HandleError("CreateIndexResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return createTextIndexResponse;
    }

    @Override
    protected CreateTextIndexResponse doInBackground(String... args) {

        InputStream inputStream = null;
        CreateTextIndexResponse result = null;

        try {

            if (!HTTPMethods.CheckIfIndexExistByStatus(_context, _indexName)) {

                URL url = URLHelper.GetCreateTextIndex(_context, _indexName, _indexDescription);

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
                    result = CreateIndexResponseFromJSON(theStringBuilder.toString());
                } else {
                    Utilities.HandleError("CreateTextIndexTask Failed to create index, status: " + response.getStatusLine().toString());
                    return null;
                }
            } else {
                Utilities.WriteLogcat("Index [" + _indexName + "] already exist.");
            }

        } catch (Exception e) {
            Utilities.HandleException(e);
        }

        return result;
    }

    protected void onPostExecute(CreateTextIndexResponse result) {

        try {
            if (_onCreateTextIndexComplete != null) {
                _onCreateTextIndexComplete.OnCreateIndexComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
