package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.interceptor.PodcastInterceptor;
import java.util.List;

public class PodcastInterceptorChain {

    private final List<PodcastInterceptor> interceptors;

    public PodcastInterceptorChain(List<PodcastInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void process(Podcast podcast) {
        for (PodcastInterceptor interceptor : interceptors) {
            if (!interceptor.process(podcast)) {
                return;
            }
        }
    }
}
