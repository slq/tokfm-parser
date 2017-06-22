package com.slq.tokfm.api;

import com.slq.scrappy.tokfm.podcast.Podcast;

import java.util.List;

public interface PodcastInfoService {
    List<Podcast> getAll();
}
