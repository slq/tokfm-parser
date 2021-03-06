package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.PodcastData;
import com.slq.scrappy.tokfm.podcast.PodcastDownloadService;
import com.slq.scrappy.tokfm.podcast.PodcastInterceptorChain;
import com.slq.scrappy.tokfm.podcast.Podcasts;
import com.slq.scrappy.tokfm.podcast.ResponseProcessor;
import com.slq.scrappy.tokfm.podcast.interceptor.FileAlreadyExistsPodcastInterceptor;
import com.slq.scrappy.tokfm.podcast.interceptor.FilenameMatchingPodcastInterceptor;
import com.slq.scrappy.tokfm.podcast.interceptor.PodcastDownloadInterceptor;
import com.slq.scrappy.tokfm.podcast.interceptor.PodcastInterceptor;
import com.slq.scrappy.tokfm.podcast.interceptor.SkipExistingFilesPodcastInterceptor;
import com.slq.scrappy.tokfm.podcast.repository.FilePodcastRepository;
import com.slq.scrappy.tokfm.podcast.repository.PodcastRepository;
import com.slq.tokfm.api.Podcast;
import com.slq.tokfm.internal.HttpPodcastInfoService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class TokFm {

	public static final String START_URL = "http://audycje.tokfm.pl/podcasts?offset=%d";

	private static final String MATCH_PATTERN_OPTION = "m";
	private static final String SKIP_EXISTING_OPTION = "s";
	private static final int ITERATION_MAX_COUNT = 100000;

	private PodcastDownloadService podcastDownloadService;
	private ResponseProcessor responseProcessor;

	public TokFm(PodcastDownloadService podcastDownloadService, ResponseProcessor responseProcessor) {
		this.podcastDownloadService = podcastDownloadService;
		this.responseProcessor = responseProcessor;
	}

	public void downloadPodcasts(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption(SKIP_EXISTING_OPTION, false, "skip existing podcasts")
				.addOption(MATCH_PATTERN_OPTION, true, "match podcast name with pattern");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		List<PodcastInterceptor> interceptors = new LinkedList<>();

		if (cmd.hasOption(MATCH_PATTERN_OPTION)) {
			interceptors.add(new FilenameMatchingPodcastInterceptor(cmd.getOptionValue(MATCH_PATTERN_OPTION)));
		}

		if (cmd.hasOption(SKIP_EXISTING_OPTION)) {
			interceptors.add(new SkipExistingFilesPodcastInterceptor());
		} else {
			PodcastRepository podcastRepository = new FilePodcastRepository();
			interceptors.add(new FileAlreadyExistsPodcastInterceptor(podcastRepository));
		}

		interceptors.add(new PodcastDownloadInterceptor(podcastDownloadService, responseProcessor));

		PodcastInterceptorChain chain = new PodcastInterceptorChain(interceptors);

		download(chain);
	}

	private void download(PodcastInterceptorChain chain) throws IOException {
		for (int offset = 0; offset < ITERATION_MAX_COUNT; offset++) {
			String url = format(START_URL, offset);
			Podcasts podcasts = podcastDownloadService.listPodcasts(url);
			podcasts.forEach(chain::process);
		}
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
		//HttpClient client = HttpClientFactory.createNtlmProxyClient();
		//HttpClient client = HttpClientFactory.createClient();
		//TokFm tokFm = new TokFm(new PodcastDownloadService(client), new ResponseProcessor());
		//tokFm.downloadPodcasts(args);

		List<PodcastData> podcasts = new HttpPodcastInfoService().getAll();
		podcasts.stream()
				.map(Podcast::getSeriesName)
				.forEach(System.out::println);
	}
}
