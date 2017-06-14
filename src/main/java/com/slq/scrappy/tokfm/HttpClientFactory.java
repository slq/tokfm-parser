package com.slq.scrappy.tokfm;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientFactory {

	private HttpClientFactory() {
	}

	public static HttpClient createClient() {
		return new DefaultHttpClient();
	}

	public static HttpClient createNtlmProxyClient() {
		final String proxyHost = Configuration.getProperty("http.proxyHost");
		final String proxyPort = Configuration.getProperty("http.proxyPort");
		final String proxyUser = Configuration.getProperty("http.proxyUser");
		final String proxyPassword = Configuration.getProperty("http.proxyPassword");

		System.setProperty("http.proxyHost", proxyHost);
		System.setProperty("http.proxyPort", proxyPort);

		DefaultHttpClient client = new DefaultHttpClient();
		client.getCredentialsProvider().setCredentials(
				new AuthScope(proxyHost, Integer.parseInt(proxyPort)),
				new NTCredentials(proxyUser + ":" + proxyPassword));

		HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		return client;
	}
}
