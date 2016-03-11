package com.topcoder.hp.idol.ondemand.Settings;

import com.topcoder.hp.idol.ondemand.RestUtils.RestConsts;

public class SettingsObject {

    private String m_ServerAddress = RestConsts.DEFAULT_SERVER_BASE_API_ADDRESS;
    private String m_ApiKey = RestConsts.DEFAULT_IDOL_API_KEY;

    public String getServerAddress() {
        return m_ServerAddress;
    }

    public void setServerAddress(String i_ServerAddress) {
        this.m_ServerAddress = i_ServerAddress;
    }

    public String getApiKey() {
        return RestConsts.API_PREFIX + m_ApiKey;
    }

    public void setApiKey(String i_ApiKey) {
        this.m_ApiKey = i_ApiKey;
    }

    public String getApiKeyWithOutPrefix() {
        return m_ApiKey;
    }
}
