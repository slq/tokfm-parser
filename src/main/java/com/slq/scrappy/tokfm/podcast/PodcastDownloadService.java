package com.slq.scrappy.tokfm.podcast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import static org.jsoup.Jsoup.connect;

public class PodcastDownloadService {

    public Podcasts listPodcasts(String url) throws IOException {
        String json = connect(url)
                .ignoreContentType(true)
                .execute()
                .body();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.fromJson(json, Podcasts.class);
    }
}
