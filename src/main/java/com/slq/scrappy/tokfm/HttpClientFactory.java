package com.slq.scrappy.tokfm;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientFactory {

    public static final String PROXY_HOST = Configuration.getProperty("http.proxyHost");
    public static final String PROXY_PORT = Configuration.getProperty("http.proxyPort");
    public static final String PROXY_USER = Configuration.getProperty("http.proxyUser");
    public static final String PROXY_PASSWORD = Configuration.getProperty("http.proxyPassword");

    public static HttpClient createClient() {
        return new DefaultHttpClient();
    }

    public static HttpClient createNtlmProxyClient() {
        System.setProperty("http.proxyHost", PROXY_HOST);
        System.setProperty("http.proxyPort", PROXY_PORT);

        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(
                new AuthScope(PROXY_HOST, Integer.parseInt(PROXY_PORT)),
                new NTCredentials(PROXY_USER + ":" + PROXY_PASSWORD));

        HttpHost proxy = new HttpHost(PROXY_HOST, Integer.parseInt(PROXY_PORT));
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        return client;
    }
}
