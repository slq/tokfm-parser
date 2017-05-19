package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.interceptor.FilenameMatchingPodcastInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class FilenameMatchingPodcastInterceptorTest {

    private static final String NOT_MATCHING_PATTERN = "not matching";
    private static final String PODCAST_FILENAME = "some text";
    private static final String MATCHING_PATTERN = "\\s+";

    @Mock private Podcast podcast;

    private FilenameMatchingPodcastInterceptor interceptor;

    @Test
    public void shouldThrowExceptionWhenFilenameDoesNotMatchPattern() {
        interceptor = new FilenameMatchingPodcastInterceptor(NOT_MATCHING_PATTERN);
        given(podcast.getTargetFilename()).willReturn(PODCAST_FILENAME);

        when(interceptor).process(podcast);

        then(caughtException()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldDoNothingWhenFilenameMatchesPattern() {
        interceptor = new FilenameMatchingPodcastInterceptor(MATCHING_PATTERN);
        given(podcast.getTargetFilename()).willReturn(PODCAST_FILENAME);

        when(interceptor).process(podcast);

        then(caughtException()).doesNotThrowAnyException();
    }
}
