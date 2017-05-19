package com.slq.scrappy.tokfm.podcast.interceptor;

import com.slq.scrappy.tokfm.podcast.Podcast;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substring;

public class SkipExistingFilesPodcastInterceptor implements PodcastInterceptor {

	private static final String HOME_DIRECTORY = System.getProperty("user.home");
	public static final int CONSOLE_LINE_WIDTH = 150;

	@Override
	public boolean process(Podcast podcast) {
		String filename = podcast.getTargetFilename();
		Path targetPath = Paths.get(HOME_DIRECTORY, "Downloads", "TokFM", filename);

		if (targetPath.toFile().exists()) {
			String shortFilename = substring(filename, 0, CONSOLE_LINE_WIDTH);
			System.out.println(format("%-150s : Skipping", shortFilename));
			return false;
		}

		return true;
	}
}
