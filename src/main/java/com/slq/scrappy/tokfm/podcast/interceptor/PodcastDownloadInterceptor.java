package com.slq.scrappy.tokfm.podcast.interceptor;

import com.slq.scrappy.tokfm.TokFmRequest;
import com.slq.scrappy.tokfm.TokFmRequestFactory;
import com.slq.scrappy.tokfm.podcast.PodcastData;
import com.slq.scrappy.tokfm.podcast.PodcastDownloadService;
import com.slq.scrappy.tokfm.podcast.ResponseProcessor;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PodcastDownloadInterceptor implements PodcastInterceptor {

	private static final String HOME_DIRECTORY = System.getProperty("user.home");

	private PodcastDownloadService podcastDownloadService;
	private ResponseProcessor responseProcessor;

	public PodcastDownloadInterceptor(PodcastDownloadService podcastDownloadService, ResponseProcessor responseProcessor) {
		this.podcastDownloadService = podcastDownloadService;
		this.responseProcessor = responseProcessor;
	}

	@Override
	public boolean process(PodcastData podcast) {
		String filename = podcast.getTargetFilename();
		Path targetPath = Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", filename);

		TokFmRequest request = TokFmRequestFactory.create(podcast);

		try {
			HttpResponse httpResponse = podcastDownloadService.executeRequest(request);
			responseProcessor.process(httpResponse, targetPath);
		} catch (IOException e) {
			System.out.println("Failed to download podcast " + podcast);
			return false;
		}

		return true;
	}
}
