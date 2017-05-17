package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.TokFmRequest;
import com.slq.scrappy.tokfm.TokFmRequestFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.http.HttpResponse;

public class PodcastDownloadInterceptor implements PodcastInterceptor {

    private static final String HOME_DIRECTORY = System.getProperty("user.home");

    private PodcastDownloadService podcastDownloadService;
    private ResponseProcessor responseProcessor;

    public PodcastDownloadInterceptor(PodcastDownloadService podcastDownloadService, ResponseProcessor responseProcessor) {
        this.podcastDownloadService = podcastDownloadService;
        this.responseProcessor = responseProcessor;
    }

    @Override
    public void process(Podcast podcast) {
        String filename = podcast.getTargetFilename();
        Path targetPath = Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", filename);

        TokFmRequest request = TokFmRequestFactory.create(podcast);

        try {
            HttpResponse httpResponse = podcastDownloadService.executeRequest(request);
            responseProcessor.process(httpResponse, targetPath);
        } catch (IOException e){
            throw new RuntimeException("Faile to download podcast " + podcast, e);
        }
    }
}
