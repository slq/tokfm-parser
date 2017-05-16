package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.Podcast;
import com.slq.scrappy.tokfm.podcast.PodcastInterceptor;

import java.util.List;

public class PodcastInterceptorChain {

    private final List<PodcastInterceptor> interceptors;

    public PodcastInterceptorChain(List<PodcastInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void process(Podcast podcast) {
        for (PodcastInterceptor interceptor : interceptors) {
            interceptor.process(podcast);
        }
    }
}
