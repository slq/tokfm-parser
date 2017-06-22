package com.slq.tokfm.api;

import com.slq.scrappy.tokfm.podcast.PodcastData;

import java.util.List;

public interface PodcastInfoService {
    List<PodcastData> getAll();
}
