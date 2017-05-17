package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.Podcast;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PodcastAssert extends AbstractAssert<PodcastAssert, Podcast> {

    private PodcastAssert(Podcast podcast, Class<?> selfType) {
        super(podcast, selfType);
    }

    public static PodcastAssert assertThat(Podcast actual) {
        return new PodcastAssert(actual, PodcastAssert.class);
    }

    public PodcastAssert hasId(String podcastId) {
        Assertions.assertThat(actual.getId()).isEqualTo(podcastId);
        return this;
    }

    public PodcastAssert hasName(String podcastName) {
        Assertions.assertThat(actual.getName()).isEqualTo(podcastName);
        return this;
    }

    public PodcastAssert hasAudio(String podcastAudio) {
        Assertions.assertThat(actual.getAudio()).isEqualTo(podcastAudio);
        return this;
    }

    public PodcastAssert hasSeriesName(String seriesName) {
        Assertions.assertThat(actual.getSeriesName()).isEqualTo(seriesName);
        return this;
    }

    public PodcastAssert hasEmissionText(String podcastEmissionText) {
        Assertions.assertThat(actual.getEmissionText()).isEqualTo(podcastEmissionText);
        return this;
    }
}
