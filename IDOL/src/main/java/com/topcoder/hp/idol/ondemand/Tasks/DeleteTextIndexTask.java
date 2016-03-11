package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.topcoder.hp.idol.ondemand.RestEntites.DeleteTextIndexResponse;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnDeleteTextIndexComplete;
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

public class DeleteTextIndexTask extends AsyncTask<String, String, DeleteTextIndexResponse> {

    private OnDeleteTextIndexComplete _onDeleteTextIndexComplete = null;
    private String _indexName = null;
    private Context _context = null;

    public DeleteTextIndexTask(Context context, String indexName, OnDeleteTextIndexComplete onDeleteTextIndexComplete) {

        _context = context;
        _indexName = indexName;
        _onDeleteTextIndexComplete = onDeleteTextIndexComplete;

        this.executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private static DeleteTextIndexResponse DeleteTextIndexResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("DeleteIndexResponseFromJSON");

        DeleteTextIndexResponse deleteIndexResponse = new DeleteTextIndexResponse();

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    deleteIndexResponse = new Gson().fromJson(resultObject.toString(), DeleteTextIndexResponse.class);
                } catch (Exception e) {
                    Utilities.HandleException(e);
                    return null;
                }
            } else {
                Utilities.HandleError("DeleteTextIndexResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return deleteIndexResponse;
    }

    @Override
    protected DeleteTextIndexResponse doInBackground(String... args) {

        InputStream inputStream = null;
        DeleteTextIndexResponse result = null;

        try {

            if (HTTPMethods.CheckIfIndexExistByStatus(_context, _indexName)) {

                URL url = URLHelper.GetDeleteTextIndexURL(_context, _indexName);

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
                    result = DeleteTextIndexResponseFromJSON(theStringBuilder.toString());

                    // Now actually delete the text index using the confirmation code
                    if (result != null &&
                            result.confirm != null &&
                            result.deleted == false) {

                        URL urlWithConfirmCode = URLHelper.GetDeleteTextIndexWithConfirmCodeURL(_context, _indexName, result.confirm);

                        HttpGet httpRequestWithConfirmCode = new HttpGet(urlWithConfirmCode.toURI());

                        HttpResponse finalResponse = (HttpResponse) httpclient.execute(httpRequestWithConfirmCode);

                        if (finalResponse.getStatusLine().toString().contains(RestConsts.RESPONSE_STATUS_OKAY)) {

                            entity = finalResponse.getEntity();
                            inputStream = entity.getContent();
                            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            theStringBuilder = new StringBuilder();

                            line = null;
                            while ((line = reader.readLine()) != null) {
                                theStringBuilder.append(line + "\n");
                            }
                            result = DeleteTextIndexResponseFromJSON(theStringBuilder.toString());
                        } else {
                            Utilities.HandleError("DeleteTextIndexTask Failed to delete index with confirmation code, status: " + response.getStatusLine().toString());
                            return null;
                        }
                    }
                } else {
                    Utilities.HandleError("DeleteTextIndexTask Failed to delete index, status: " + response.getStatusLine().toString());
                    return null;
                }
            } else {
                Utilities.WriteLogcat("Index [" + _indexName + "] doesn't exist.");
            }

        } catch (Exception e) {
            Utilities.HandleException(e);
        }

        return result;
    }

    protected void onPostExecute(DeleteTextIndexResponse result) {

        try {
            if (_onDeleteTextIndexComplete != null) {
                _onDeleteTextIndexComplete.OnDeleteTextIndexComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
