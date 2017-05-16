package com.slq.scrappy.tokfm.podcast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slq.scrappy.tokfm.TokFmRequest;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;


import static org.apache.http.HttpStatus.SC_OK;
import static org.jsoup.Jsoup.connect;

public class PodcastDownloadService {

    public Podcasts listPodcasts(String url) throws IOException {
        String json = "";
        try {
            json = connect(url)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (SocketTimeoutException exception) {
            System.out.println("Retrying: " + url);
            return listPodcasts(url);
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.fromJson(json, Podcasts.class);
    }

    public HttpResponse executeRequest(TokFmRequest request) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != SC_OK) {
            System.err.println(response.getStatusLine().getReasonPhrase());
        }

        return response;
    }
}
