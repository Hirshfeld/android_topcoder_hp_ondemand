package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.topcoder.hp.idol.ondemand.RestEntites.Index;
import com.topcoder.hp.idol.ondemand.RestEntites.ListIndexesResponse;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnListIndexesComplete;
import com.topcoder.hp.idol.ondemand.RestUtils.HTTPMethods;
import com.topcoder.hp.idol.ondemand.RestUtils.RestConsts;
import com.topcoder.hp.idol.ondemand.RestUtils.URLHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ListIndexesTask extends AsyncTask<String, String, ListIndexesResponse> {

    private OnListIndexesComplete _onListIndexesComplete = null;
    private Context _context = null;

    public ListIndexesTask(Context context, OnListIndexesComplete onListIndexesComplete) {

        _context = context;
        _onListIndexesComplete = onListIndexesComplete;

        this.execute();
    }

    private static ListIndexesResponse ListIndexesResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("ListIndexesResponseFromJSON");

        ListIndexesResponse listIndexesResponse = new ListIndexesResponse();

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    JSONArray publicIndexesArray;
                    publicIndexesArray = resultObject.getJSONArray("public_index");//.getJSONArray("groups");
                    JSONArray indexesArray;
                    indexesArray = resultObject.getJSONArray("index");//.getJSONArray("groups");

                    for (int i = 0; i < indexesArray.length(); i++) {

                        Index currentIndex = new Gson().fromJson(indexesArray.getJSONObject(i).toString(), Index.class);
                        listIndexesResponse.indexes.add(currentIndex);
                    }
                } catch (Exception e) {
                    Utilities.HandleException(e);
                    return null;
                }
            } else {
                Utilities.HandleError("ListIndexesResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return listIndexesResponse;
    }

    @Override
    protected ListIndexesResponse doInBackground(String... args) {

        InputStream inputStream = null;
        ListIndexesResponse result = null;

        try {

            URL url = URLHelper.GetListIndexesURL(_context);

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
                result = ListIndexesResponseFromJSON(theStringBuilder.toString());
            } else {

                Utilities.HandleError("AddTextToIndexResponse Failed to list indexes, status: [" + response.getStatusLine().toString() + "] URL is: [" + url + "]");

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

    protected void onPostExecute(ListIndexesResponse result) {

        try {
            if (_onListIndexesComplete != null) {
                _onListIndexesComplete.OnListIndexesComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
