package com.slq.scrappy.tokfm.podcast.repository;

import java.nio.file.Paths;

public class FilePodcastRepository implements PodcastRepository {

    private static final String HOME_DIRECTORY = System.getProperty("user.home");

    @Override
    public boolean exists(String name) {
        return Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", name).toFile().exists();
    }
}
