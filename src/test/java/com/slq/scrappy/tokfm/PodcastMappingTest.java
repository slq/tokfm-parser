package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.Podcast;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.slq.scrappy.tokfm.JsonObjectMapper.createJsonObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class PodcastMappingTest {

	private static final String PODCAST_ID = "podcastId";
	private static final String PODCAST_NAME = "podcastName";
	private static final String PODCAST_AUDIO = "podcastAudio";
	private static final String PODCAST_SERIES_NAME = "podcastSeriesName";
	private static final String PODCAST_EMISSION_TEXT = "podcastEmissionText";
	private static final String PODCAST_ID_KEY = "podcast_id";
	private static final String PODCAST_NAME_KEY = "podcast_name";
	private static final String PODCAST_AUDIO_KEY = "podcast_audio";
	private static final String PODCAST_SERIES_NAME_KEY = "series_name";
	private static final String PODCAST_EMISSION_TEXT_KEY = "podcast_emission_text";

	@Test
	public void shouldReadPayloadFromJson() {
		String json = createJson();

		Podcast podcast = createJsonObjectMapper().readValue(json, Podcast.class);

		PodcastAssert.assertThat(podcast)
				.hasId(PODCAST_ID)
				.hasName(PODCAST_NAME)
				.hasAudio(PODCAST_AUDIO)
				.hasSeriesName(PODCAST_SERIES_NAME)
				.hasEmissionText(PODCAST_EMISSION_TEXT);
	}

	@Test
	public void shouldWritePodcastToJson() {
		Podcast podcast = createJsonObjectMapper().readValue(createJson(), Podcast.class);

		String json = createJsonObjectMapper().writeValueAsString(podcast);
		JSONObject jsonObject = new JSONObject(json);

		assertThat(jsonObject.get(PODCAST_ID_KEY)).isEqualTo(PODCAST_ID);
		assertThat(jsonObject.get(PODCAST_NAME_KEY)).isEqualTo(PODCAST_NAME);
		assertThat(jsonObject.get(PODCAST_AUDIO_KEY)).isEqualTo(PODCAST_AUDIO);
		assertThat(jsonObject.get(PODCAST_SERIES_NAME_KEY)).isEqualTo(PODCAST_SERIES_NAME);
		assertThat(jsonObject.get(PODCAST_EMISSION_TEXT_KEY)).isEqualTo(PODCAST_EMISSION_TEXT);
	}

	private String createJson() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(PODCAST_ID_KEY, PODCAST_ID);
		parameters.put(PODCAST_NAME_KEY, PODCAST_NAME);
		parameters.put(PODCAST_AUDIO_KEY, PODCAST_AUDIO);
		parameters.put(PODCAST_SERIES_NAME_KEY, PODCAST_SERIES_NAME);
		parameters.put(PODCAST_EMISSION_TEXT_KEY, PODCAST_EMISSION_TEXT);
		parameters.put("some_unregognized_property", "unrecognizedPropertyValue");
		JSONObject json = new JSONObject(parameters);

		return json.toString();
	}
}
