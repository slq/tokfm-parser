package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.Podcast;
import com.slq.scrappy.tokfm.podcast.PodcastInterceptor;

import java.nio.file.Path;
import java.nio.file.Paths;


import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substring;

public class SkipExistingFilesPodcastInterceptor implements PodcastInterceptor {

    private static final String HOME_DIRECTORY = System.getProperty("user.home");

    @Override
    public void process(Podcast podcast) {
        String filename = podcast.getTargetFilename();
        Path targetPath = Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", filename);

        if (targetPath.toFile().exists()) {
            String shortFilename = substring(filename, 0, 150);
            System.out.println(format("%-150s : Skipping", shortFilename));
        }
    }
}
