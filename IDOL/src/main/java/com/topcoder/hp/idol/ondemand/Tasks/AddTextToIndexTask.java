package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.topcoder.hp.idol.ondemand.RestEntites.AddTextToIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnAddTextComplete;
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

public class AddTextToIndexTask extends AsyncTask<String, String, AddTextToIndexResponse> {

    private OnAddTextComplete _onAddTextComplete = null;
    private String _indexName = null;
    private TextDocument _textDocument = null;
    private Context _context = null;

    public AddTextToIndexTask(Context context, String indexName, TextDocument textDocument, OnAddTextComplete onAddTextComplete) {

        _context = context;
        _indexName = indexName;
        _textDocument = textDocument;
        _onAddTextComplete = onAddTextComplete;

        this.execute();
    }

    private static AddTextToIndexResponse AddTextToIndexResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("AddTextToIndexResponseFromJSON");

        AddTextToIndexResponse addTextToIndexResponse = null;

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);

                    addTextToIndexResponse = new AddTextToIndexResponse(resultObject); //new Gson().fromJson(resultObject.toString(), AddTextToIndexResponse.class);
                } catch (Exception e) {
                    Utilities.HandleError("AddTextToIndexResponseFromJSON - No message found");
                    return null;
                }
            } else {
                Utilities.HandleError("AddTextToIndexResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return addTextToIndexResponse;
    }

    @Override
    protected AddTextToIndexResponse doInBackground(String... args) {

        InputStream inputStream = null;
        AddTextToIndexResponse result = null;

        try {

            if (HTTPMethods.CheckIfIndexExistByStatus(_context, _indexName)) {

                URL url = URLHelper.PutAddTextToIndex(_context, _indexName, _textDocument);

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
                    result = AddTextToIndexResponseFromJSON(theStringBuilder.toString());
                } else {

                    Utilities.HandleError("AddTextToIndexResponse Failed to add text to index, status: [" + response.getStatusLine().toString() + "] URL is: [" + url + "]");

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
            } else {
                Utilities.WriteLogcat("Index [" + _indexName + "] already exist.");
            }

        } catch (Exception e) {
            Utilities.HandleException(e);
        }

        return result;
    }

    protected void onPostExecute(AddTextToIndexResponse result) {

        try {
            if (_onAddTextComplete != null) {
                _onAddTextComplete.OnAddTextComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
