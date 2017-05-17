package com.slq.scrappy.tokfm.podcast;

import java.nio.file.Path;
import java.nio.file.Paths;


import static org.apache.commons.lang3.StringUtils.substring;

public class FileAlreadyExistsPodcastInterceptor implements PodcastInterceptor {

    private static final String HOME_DIRECTORY = System.getProperty("user.home");

    @Override
    public void process(Podcast podcast) {
        String filename = podcast.getTargetFilename();
        Path targetPath = Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", filename);

        if (targetPath.toFile().exists()) {
            String shortFilename = substring(filename, 0, 150);
            String message = String.format("%-150s : Already exists. Exiting...", shortFilename);
            System.out.println(message);
            throw new IllegalArgumentException(message);
        }
    }
}
