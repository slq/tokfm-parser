package com.slq.scrappy.tokfm.podcast.interceptor;

import com.slq.scrappy.tokfm.podcast.Podcast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameMatchingPodcastInterceptor implements PodcastInterceptor {

	private String pattern;

	public FilenameMatchingPodcastInterceptor(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean process(Podcast podcast) {
		return matching(pattern, podcast.getTargetFilename());
	}

	private boolean matching(String matchPattern, String text) {
		Pattern pattern = Pattern.compile(matchPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			System.out.println("Found matched text: (" + matcher.group() + ")");
			return true;
		}
		return false;
	}
}
