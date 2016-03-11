package com.topcoder.hp.idol.ondemand.RestUtils;

public class RestConsts {


    public static final String EXECUTION_MODE_SYNC = "sync";
    public static final String URL_CHECK_INDEX_STATUS = EXECUTION_MODE_SYNC + "/indexstatus/v1?index=%s" + "%s";
    public static final String URL_ADD_TEXT_TO_INDEX_JSON = EXECUTION_MODE_SYNC + "/addtotextindex/v1?json=%s&index=%s&additional_metadata=%s&reference_prefix=%s" + "%s";
    public static final String URL_ADD_TEXT_ATTACHMENT_TO_INDEX = EXECUTION_MODE_SYNC + "/addtotextindex/v1";
    public static final String URL_PERFORM_SENTIMENT_ANALYSIS_SYNC = EXECUTION_MODE_SYNC + "/analyzesentiment/v1?text=%s" + "%s";
    public static final String URL_PERFORM_SENTIMENT_ANALYSIS_FOR_FILE = EXECUTION_MODE_SYNC + "/analyzesentiment/v1";
    public static final String URL_LANGUAGE_IDENTIFICATION_SYNC = EXECUTION_MODE_SYNC + "/identifylanguage/v1?text=%s&additional_metadata=true" + "%s";
    public static final String URL_LIST_INDEXES = EXECUTION_MODE_SYNC + "/listindexes/v1?type=%s&flavor=%s" + "%s";
    public static final String URL_DELETE_TEXT_INDEX = EXECUTION_MODE_SYNC + "/deletetextindex/v1?index=%s" + "%s";
    public static final String URL_DELETE_TEXT_INDEX_WITH_CONFIRM_CODE = EXECUTION_MODE_SYNC + "/deletetextindex/v1?index=%s&confirm=%s" + "%s";
    public static final String EXECUTION_MODE_ASYNC = "async";
    public static final String URL_STORE_OBJECT = EXECUTION_MODE_ASYNC + "/storeobject/v1?file=%s" + "%s";
    public static final String FLAVOR_MODE = "&flavor=explorer";
    public static final String URL_CREATE_TEXT_INDEX = EXECUTION_MODE_SYNC + "/createtextindex/v1?index=%s&description=%s" + "%s" + FLAVOR_MODE;
    public static final String DEFAULT_SERVER_BASE_API_ADDRESS = "https://api.idolondemand.com/1/api/";
    public static final String DEFAULT_IDOL_API_KEY = "cdf49062-3d85-443b-9ea8-371ed1113b43";
    public static final String API_PREFIX = "&apikey=";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_TEXT = "application/text";
    public static final String DOCUMENT_WRAPPER_NAME = "document";

    public static final String RESPONSE_STATUS_OKAY = "200";
}
