package com.slq.scrappy.tokfm.podcast.interceptor;

import com.slq.scrappy.tokfm.podcast.PodcastData;

public interface PodcastInterceptor {
	boolean process(PodcastData podcast);
}
