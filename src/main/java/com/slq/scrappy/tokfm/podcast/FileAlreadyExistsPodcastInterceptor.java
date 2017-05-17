package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.repository.PodcastRepository;


import static org.apache.commons.lang3.StringUtils.substring;

public class FileAlreadyExistsPodcastInterceptor implements PodcastInterceptor {

    private PodcastRepository podcastRepository;

    public FileAlreadyExistsPodcastInterceptor(PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }

    @Override
    public void process(Podcast podcast) {
        String filename = podcast.getTargetFilename();

        if (podcastRepository.exists(filename)) {
            String shortFilename = substring(filename, 0, 150);
            String message = String.format("%-150s : Already exists. Exiting...", shortFilename);
            System.out.println(message);
            throw new IllegalArgumentException("Podcast: [" + shortFilename + "] already exists");
        }
    }
}
