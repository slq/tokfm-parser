package com.slq.scrappy.tokfm.podcast.interceptor;

import com.slq.scrappy.tokfm.podcast.Podcast;

public interface PodcastInterceptor {
	boolean process(Podcast podcast);
}
