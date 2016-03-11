package com.topcoder.hp.idol.ondemand.RestUtils;

import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier {

    public boolean verify(String hostname, SSLSession session) {
        Utilities.WriteLogcat("NullHostNameVerifier: Approving certificate for " + hostname);
        return true;
    }

}
