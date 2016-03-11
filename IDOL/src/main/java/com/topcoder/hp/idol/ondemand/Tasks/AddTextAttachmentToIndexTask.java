package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.topcoder.hp.idol.ondemand.RestEntites.AddTextAttachmentToIndexResponse;
import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnAddTextAttachmentComplete;
import com.topcoder.hp.idol.ondemand.RestUtils.HTTPMethods;
import com.topcoder.hp.idol.ondemand.RestUtils.RestConsts;
import com.topcoder.hp.idol.ondemand.RestUtils.URLHelper;
import com.topcoder.hp.idol.ondemand.Settings.SettingsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class AddTextAttachmentToIndexTask extends AsyncTask<String, String, AddTextAttachmentToIndexResponse> {

    private OnAddTextAttachmentComplete _onAddTextAttachmentComplete = null;
    private String _indexName = null;
    private TextDocument _textDocument = null;
    private Context _context = null;
    private String _filePath = null;
    private String _mimeType = null;
    private JSONArray _additionalData = null;


    public AddTextAttachmentToIndexTask(Context context, String indexName, String mimeType, String filePath, JSONArray additionalData, OnAddTextAttachmentComplete onAddTextAttachmentComplete) {

        _context = context;
        _indexName = indexName;
        _onAddTextAttachmentComplete = onAddTextAttachmentComplete;
        _filePath = filePath;
        _mimeType = mimeType;
        _additionalData = additionalData;
        this.execute();
    }

    private static AddTextAttachmentToIndexResponse AddTextAttachmentToIndexResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("AddTextAttachmentToIndexResponseFromJSON");

        AddTextAttachmentToIndexResponse addTextAttachmentToIndexResponse = null;

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    addTextAttachmentToIndexResponse = new AddTextAttachmentToIndexResponse(resultObject); //new Gson().fromJson(resultObject.toString(), AddTextAttachmentToIndexResponse.class);
                } catch (Exception e) {
                    Utilities.HandleError("AddTextAttachmentToIndexResponseFromJSON - No message found");
                    return null;
                }
            } else {
                Utilities.HandleError("AddTextAttachmentToIndexResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return addTextAttachmentToIndexResponse;
    }

    @Override
    protected AddTextAttachmentToIndexResponse doInBackground(String... args) {

        InputStream inputStream = null;
        AddTextAttachmentToIndexResponse result = null;
        String charset = "UTF-8";

        try {
            final File file = new File(_filePath);
            if (HTTPMethods.CheckIfIndexExistByStatus(_context, _indexName)) {

                URL url = URLHelper.PostAddTextAttachmentToIndex(_context);
                Utilities.WriteLogcat(url.toString());
                HttpPost httpRequest = new HttpPost(url.toURI());
                HttpClient httpclient = HTTPMethods.getSSLIgnorateHttpClient(); //new DefaultHttpClient();

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                FileBody fb = new FileBody(file);

                //addBinaryBody(java.lang.String name, java.io.File file, org.apache.http.entity.ContentType contentType, java.lang.String filename
                builder.addBinaryBody("file", file, org.apache.http.entity.ContentType.create(_mimeType), file.getName());
                builder.addTextBody("apikey", SettingsManager.GetAPIKeyWithoutPrefix(_context));
                builder.addTextBody("index", _indexName);
                if (_additionalData != null) {
                    builder.addTextBody("additional_metadata", _additionalData.toString());
                }
                final HttpEntity fileEntity = builder.build();

                httpRequest.setEntity(fileEntity);
                HttpResponse response = httpclient.execute(httpRequest);

                if (response.getStatusLine().toString().contains(RestConsts.RESPONSE_STATUS_OKAY)) {

                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder theStringBuilder = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        theStringBuilder.append(line + "\n");
                    }
                    result = AddTextAttachmentToIndexResponseFromJSON(theStringBuilder.toString());
                } else {

                    Utilities.HandleError("addTextAttachmentToIndexResponse Failed to add text attachment to index, status: [" + response.getStatusLine().toString() + "] URL is: [" + url + "]");

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
            if (e != null) {
                Utilities.HandleException(e);
            } else {
                Utilities.HandleError("Error when adding attachment");
            }
        }
        return result;
    }

    protected void onPostExecute(AddTextAttachmentToIndexResponse result) {

        try {
            if (_onAddTextAttachmentComplete != null) {
                _onAddTextAttachmentComplete.OnAddTextAttachmentComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
