package com.topcoder.hp.idol.ondemand.Tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.topcoder.hp.idol.ondemand.RestEntites.SentimentAnalysis;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Interfaces.OnPerformSentimentAnalysisComplete;
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
import java.net.URLConnection;

public class PerformSentimentAnalysisForFileTask extends AsyncTask<String, String, SentimentAnalysis> {

    private OnPerformSentimentAnalysisComplete _onPerformSentimentAnalysisComplete = null;
    private String _filePath = null;
    private Context _context = null;

    public PerformSentimentAnalysisForFileTask(Context context, String filePath, OnPerformSentimentAnalysisComplete onPerformSentimentAnalysisComplete) {

        _context = context;
        _filePath = filePath;
        _onPerformSentimentAnalysisComplete = onPerformSentimentAnalysisComplete;

        this.execute();
    }

    private static SentimentAnalysis SentimentAnalysisResponseFromJSON(String result) throws JSONException {

        Utilities.WriteLogcat("SentimentAnalysisResponseFromJSON");

        SentimentAnalysis sentimentAnalysis = null;

        try {
            if (result != null) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    JSONArray positiveArray = resultObject.getJSONArray("positive");
                    JSONArray negativeArray = resultObject.getJSONArray("negative");
                    JSONObject aggregateObject = resultObject.getJSONObject("aggregate");
                    sentimentAnalysis = new SentimentAnalysis(positiveArray, negativeArray, aggregateObject);
                } catch (Exception e) {
                    Utilities.HandleException(e);
                    return null;
                }
            } else {
                Utilities.HandleError("SentimentAnalysisResponseFromJSON - Result is NULL");
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return sentimentAnalysis;
    }

    @Override
    protected SentimentAnalysis doInBackground(String... args) {

        InputStream inputStream = null;
        SentimentAnalysis result = null;

        try {
            URL url = URLHelper.PostPerformSentimentAnalysisForFileURL(_context);
            File file = new File(_filePath);
            Utilities.WriteLogcat(url.toString());
            HttpPost httpRequest = new HttpPost(url.toURI());
            HttpClient httpclient = HTTPMethods.getSSLIgnorateHttpClient(); //new DefaultHttpClient();

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            FileBody fb = new FileBody(file);

            //addBinaryBody(java.lang.String name, java.io.File file, org.apache.http.entity.ContentType contentType, java.lang.String filename
            builder.addBinaryBody("file", file, org.apache.http.entity.ContentType.create(URLConnection.guessContentTypeFromName(_filePath)), file.getName());
            builder.addTextBody("apikey", SettingsManager.GetAPIKeyWithoutPrefix(_context));

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
                result = SentimentAnalysisResponseFromJSON(theStringBuilder.toString());
            } else {

                Utilities.HandleError("PerformSentimentAnalysisForFileTask Failed perform sentiment analysis on file, status: [" + response.getStatusLine().toString() + "] URL is: [" + url + "]");

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

    protected void onPostExecute(SentimentAnalysis result) {

        try {
            if (_onPerformSentimentAnalysisComplete != null) {
                _onPerformSentimentAnalysisComplete.OnPerformSentimentAnalysisComplete(result);
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

}
