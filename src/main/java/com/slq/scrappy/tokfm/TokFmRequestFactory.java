package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.PodcastData;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class TokFmRequestFactory {

	private TokFmRequestFactory() {
	}

	public static TokFmRequest create(PodcastData podcast) {
		try {
			return TokFmRequest.from(podcast);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}
}
