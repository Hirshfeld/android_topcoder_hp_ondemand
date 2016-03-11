package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.topcoder.hp.idol.ondemand.RestEntites.StoreObjectResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnStoreObjectComplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class StoreObjectTask extends AsyncTask<String, String, StoreObjectResponse> {

    private OnStoreObjectComplete _onStoreObjectCompleteComplete = null;
    private String _indexName = null;
    private TextDocument _textDocument = null;
    private Context _context = null;

    public StoreObjectTask(Context context, String indexName, TextDocument textDocument, OnStoreObjectComplete onStoreObjectCompleteComplete) {

        _context = context;
        _indexName = indexName;
        _textDocument = textDocument;
        _onStoreObjectCompleteComplete = onStoreObjectCompleteComplete;

        this.execute();
    }

    private static StoreObjectResponse StoreObjectResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("StoreObjectResponseFromJSON");

        StoreObjectResponse storeObjectResponse = new StoreObjectResponse();

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    storeObjectResponse = new Gson().fromJson(resultObject.toString(), StoreObjectResponse.class);
                } catch (Exception e) {
                    Utilities.HandleError("StoreObjectResponseFromJSON - No message found");
                    return null;
                }
            } else {
                Utilities.HandleError("StoreObjectResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return storeObjectResponse;
    }

    @Override
    protected StoreObjectResponse doInBackground(String... args) {

        InputStream inputStream = null;
        StoreObjectResponse result = null;

        try {

           /* if (HTTPMethods.CheckIfIndexExist(_context, _indexName)) {

                URL url = URLHelper.(_context, _indexName, _textDocument);

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

                    Utilities.HandleError("AddTextToIndexResponse Failed to add text to index, status: [" + response.getStatusLine().toString() +"] URL is: [" + url + "]");

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
                    } catch(Exception e) {}

                    return null;
                }
            } else {
                Utilities.WriteLogcat("Index [" + _indexName + "] already exist.");
            }*/

        } catch (Exception e) {
            Utilities.HandleException(e);
        }

        return result;
    }

    protected void onPostExecute(StoreObjectResponse result) {

        try {
            if (_onStoreObjectCompleteComplete != null) {
                _onStoreObjectCompleteComplete.OnStoreObjectComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
