package com.topcoder.hp.idol.ondemand.RestUtils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HTTPMethods {

    public static HttpClient getSSLIgnorateHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new CASSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            Utilities.HandleException(e);
            return new DefaultHttpClient();
        }
    }

    @SuppressLint("TrulyRandom")
    public static void DisableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {

            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static boolean CheckIfIndexExistByStatus(Context context, String indexName) {
        boolean isExist = false;
        try {
            URL url = URLHelper.GetCheckIndexStatus(context, indexName);

            HttpGet httpRequest = new HttpGet(url.toURI());
            HttpClient httpclient = HTTPMethods.getSSLIgnorateHttpClient();

            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

            if (response.getStatusLine().toString().contains(RestConsts.RESPONSE_STATUS_OKAY)) {
                isExist = true;
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
        return isExist;


    }

    public static String PrepareTextDocumentForAddText(TextDocument textDocument) {

        try {
            Gson gson = new Gson();
            String str_documentObject = gson.toJson(textDocument, TextDocument.class);
            JsonParser parser = new JsonParser();
            JsonObject documentObject = (JsonObject) parser.parse(str_documentObject);

            JsonObject documentsWrapper = new JsonObject();
            documentsWrapper.add(RestConsts.DOCUMENT_WRAPPER_NAME, documentObject);

            String str_documentsWrapper = gson.toJson(documentsWrapper);
            return URLEncoder.encode(str_documentsWrapper, "UTF-8");
        } catch (Exception e) {
            Utilities.HandleException(e);
            return null;
        }

    }


    public static String EncodeHTML(String str) {
        str = str.replaceAll("-", "%2D");
        str = str.replaceAll(":", "%3A");
        return str.replaceAll(" ", "%20");

        /*try {
            return  URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            Utilities.HandleException(e);
            return null;
        }*/
    }


}
