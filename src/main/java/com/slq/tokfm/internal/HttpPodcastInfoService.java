package com.slq.tokfm.internal;

import com.slq.scrappy.tokfm.podcast.Podcast;
import com.slq.tokfm.api.PodcastInfoService;

import java.util.List;

import static java.util.Collections.emptyList;

public class HttpPodcastInfoService implements PodcastInfoService {

    @Override
    public List<Podcast> getAll() {
        return emptyList();
    }
}
