package com.topcoder.hp.idol.ondemand.RestUtils;

import android.content.Context;

import com.topcoder.hp.idol.ondemand.RestEntites.TextDocument;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Settings.SettingsManager;

import java.net.URL;
import java.net.URLEncoder;

public class URLHelper {

    public static URL GetDeleteTextIndexWithConfirmCodeURL(Context context, String indexName, String confirmCode) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) + String.format(RestConsts.URL_DELETE_TEXT_INDEX_WITH_CONFIRM_CODE, HTTPMethods.EncodeHTML(indexName), HTTPMethods.EncodeHTML(confirmCode), SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL GetDeleteTextIndexURL(Context context, String indexName) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) + String.format(RestConsts.URL_DELETE_TEXT_INDEX, HTTPMethods.EncodeHTML(indexName), SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL GetCreateTextIndex(Context context, String indexName, String indexDescription) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) + String.format(RestConsts.URL_CREATE_TEXT_INDEX, HTTPMethods.EncodeHTML(indexName), HTTPMethods.EncodeHTML(indexDescription), SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL GetCheckIndexStatus(Context context, String indexName) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) + String.format(RestConsts.URL_CHECK_INDEX_STATUS, HTTPMethods.EncodeHTML(indexName), SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL PutAddTextToIndex(Context context, String indexName, TextDocument textDocument) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_ADD_TEXT_TO_INDEX_JSON,
                                    HTTPMethods.PrepareTextDocumentForAddText(textDocument),
                                    indexName,
                                    "", //additional_metadata
                                    "", //reference_prefix
                                    SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL PostAddTextAttachmentToIndex(Context context) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_ADD_TEXT_ATTACHMENT_TO_INDEX));

        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL PostPerformSentimentAnalysisForFileURL(Context context) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_PERFORM_SENTIMENT_ANALYSIS_FOR_FILE));

        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }


    public static URL GetPerformSentimentAnalysisURL(Context context, String text) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_PERFORM_SENTIMENT_ANALYSIS_SYNC,
                                    URLEncoder.encode(text, "UTF-8"),
                                    SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL GetLanguageIdentificationURL(Context context, String text) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_LANGUAGE_IDENTIFICATION_SYNC,
                                    URLEncoder.encode(text, "UTF-8"),
                                    SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL PostStoreObject(Context context, String fileName) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_STORE_OBJECT,
                                    fileName,
                                    SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static URL GetListIndexesURL(Context context) {
        try {
            return new URL(
                    SettingsManager.GetServerAddress(context) +
                            String.format(RestConsts.URL_LIST_INDEXES,
                                    "", // type
                                    "", // flavor
                                    SettingsManager.GetAPIKey(context)));
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }


}
