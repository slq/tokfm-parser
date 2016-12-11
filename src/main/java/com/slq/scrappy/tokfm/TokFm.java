package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.Podcast;
import com.slq.scrappy.tokfm.podcast.PodcastDownloadService;
import com.slq.scrappy.tokfm.podcast.Podcasts;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substring;

public class TokFm {

	private static final String HOME_DIRECTORY = System.getProperty("user.home");
	private static final String MD5_PHRASE = "MwbJdy3jUC2xChua/";
	private static final String START_URL = "http://audycje.tokfm.pl/podcasts?offset=%d";

	private static final String LIST_OPTION = "l";
	private static final String MATCH_PATTERN_OPTION = "m";
	private static final String SKIP_EXISTING_OPTION = "s";
	public static final int ITERATION_MAX_COUNT = 100000;

	private PodcastDownloadService podcastDownloadService;

	public void setPodcastDownloadService(PodcastDownloadService podcastDownloadService) {
		this.podcastDownloadService = podcastDownloadService;
	}

	public void downloadPodcasts(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption(LIST_OPTION, false, "list podcasts")
				.addOption(SKIP_EXISTING_OPTION, false, "skip existing podcasts")
				.addOption(MATCH_PATTERN_OPTION, true, "match podcast name with pattern");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption(LIST_OPTION)) {
			String matchPattern = cmd.getOptionValue(MATCH_PATTERN_OPTION);
			list(matchPattern);
		} else {
			String matchPattern = cmd.getOptionValue(MATCH_PATTERN_OPTION);
			boolean skipOption = cmd.hasOption(SKIP_EXISTING_OPTION);
			download(matchPattern, skipOption);
		}
	}

	private void list(String matchPattern) throws IOException{
		for (int offset = 0; offset < 100000; offset++) {
			String url = format(START_URL, offset);
			podcastDownloadService.listPodcasts(url)
					.forEach()
					.filter(podcast -> matching(matchPattern, podcast.getName()))
					.map(Podcast::getTargetFilename)
					.forEach(System.out::println);
		}
	}

	private boolean matching(String matchPattern, String text) {
		if(isBlank(matchPattern)){
			return true;
		}

		Pattern pattern = Pattern.compile(matchPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			System.out.println("Matched: " + matcher.group());
			return true;
		}
		return false;
	}

	private void download(String matchPattern, boolean skipOption) throws IOException, NoSuchAlgorithmException {
		//          String startUrl = "http://audycje.tokfm.pl/podcasts?offset=%d&series_id=11";
		for (int offset = 0; offset < ITERATION_MAX_COUNT; offset++) {
			String url = format(START_URL, offset);

			Podcasts podcasts = podcastDownloadService.listPodcasts(url);

			for (Podcast podcast : podcasts.getPodcasts()) {
				if (!matching(matchPattern, podcast.getName())) {
					continue;
				}

				String filename = podcast.getTargetFilename();
				Path targetPath = Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", filename);

				if (targetPath.toFile().exists()) {
					String shortFilename = substring(filename, 0, 150);
					if (skipOption) {
						System.out.println(format("%-150s : Skipping", shortFilename));
						continue;
					} else {
						System.out.println(format("%-150s : Already exists. Exiting...", shortFilename));
						return;
					}
				}

				HttpPost request = prepareRequest(podcast);
				HttpEntity entity = sendRequest(request);
				downloadRequestedFile(toStream(entity), toStream(targetPath));
			}
		}
	}

	private DownloadCountingOutputStream toStream(Path targetPath) throws FileNotFoundException {
		return new DownloadCountingOutputStream(targetPath);
	}

	private InputStream toStream(HttpEntity entity) throws IOException {
		return entity.getContent();
	}

	private void downloadRequestedFile(InputStream inputStream, OutputStream outputStream) throws IOException {
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();
		System.out.println();
	}

	private HttpEntity sendRequest(HttpPost request) throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);
		return response.getEntity();
	}

	private HttpPost prepareRequest(Podcast podcast) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String hexTime = currentTimeSecondsToHex();
		String audioName = podcast.getAudio();

		byte[] digest = digest(hexTime, audioName);
		BigInteger bigInt = new BigInteger(1, digest);

		String mdp = bigInt.toString(16).toUpperCase();
		// not needed?
		mdp = StringUtils.leftPad(mdp, 32, '0');
		String load = hexTime + "." + mdp.substring(12, 16) + "." + mdp.substring(8, 12) + "." + mdp.substring(16, 20) + "." + mdp.substring(20, 24) + ".";

		Random random = new Random();
		String token = format(valueOf(random.nextInt(255)), 'x').toUpperCase() + "." + mdp.substring(0, 4) + "." + mdp.substring(4, 8) + "." + mdp.substring(28, 32) + "." + mdp.substring(24, 28) + ".";

		String fileId = podcast.getId();
		String uri = format("http://storage.tuba.fm/load_podcast/%s.mp3", fileId);

		HttpPost request = new HttpPost(uri);
		request.addHeader("X-Tuba", audioName);
		request.addHeader("X-Tuba-Load", load);
		request.addHeader("X-Tuba-Token", token);
		return request;
	}

	private String currentTimeSecondsToHex() {
		return format("%x", (int) Math.floor(System.currentTimeMillis() / 1000)).toUpperCase();
	}

	private byte[] digest(String n, String audioName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String message = MD5_PHRASE + audioName + n;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] bytes = message.getBytes("UTF-8");
		return md5.digest(bytes);
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
		TokFm tokFm = new TokFm();
		tokFm.setPodcastDownloadService(new PodcastDownloadService());
		tokFm.downloadPodcasts(args);
	}
}
