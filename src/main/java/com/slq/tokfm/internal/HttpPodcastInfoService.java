package com.slq.tokfm.internal;

import com.slq.scrappy.tokfm.podcast.PodcastData;
import com.slq.scrappy.tokfm.podcast.Podcasts;
import com.slq.tokfm.api.PodcastInfoService;

import java.io.IOException;
import java.util.List;

import static com.slq.scrappy.tokfm.JsonObjectMapper.createJsonObjectMapper;
import static org.jsoup.Jsoup.connect;

public class HttpPodcastInfoService implements PodcastInfoService {

    @Override
    public List<PodcastData> getAll() {
        String url = String.format("http://audycje.tokfm.pl/podcasts?offset=%d", 1);
        String json = null;
        try {
            json = connect(url)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Podcasts podcasts = createJsonObjectMapper().readValue(json, Podcasts.class);
        return podcasts.getPodcasts();
    }
}
