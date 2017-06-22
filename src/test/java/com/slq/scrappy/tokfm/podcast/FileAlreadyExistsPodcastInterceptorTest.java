package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.interceptor.FileAlreadyExistsPodcastInterceptor;
import com.slq.scrappy.tokfm.podcast.repository.PodcastRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class FileAlreadyExistsPodcastInterceptorTest {

	private static final String PODCAST_NAME = "someName";

	@Mock private PodcastRepository podcastRepository;
	@Mock private PodcastData podcast;

	private FileAlreadyExistsPodcastInterceptor interceptor;

	@Before
	public void setUp() {
		given(podcast.getTargetFilename()).willReturn(PODCAST_NAME);
		interceptor = new FileAlreadyExistsPodcastInterceptor(podcastRepository);
	}

	@Test
	public void shouldThrowExceptionWhenFileAlreadyExists() {
		given(podcastRepository.exists(PODCAST_NAME)).willReturn(true);

		when(interceptor).process(podcast);

		then(caughtException()).isExactlyInstanceOf(RuntimeException.class);
	}

	@Test
	public void shouldReturnTrueWhenFileDoesNotExist() {
		given(podcastRepository.exists(PODCAST_NAME)).willReturn(false);

		boolean shouldContinue = interceptor.process(podcast);

		assertThat(shouldContinue).isTrue();
	}
}
