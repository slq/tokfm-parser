package com.slq.scrappy.tokfm.podcast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Podcasts {

	@JsonProperty
	private List<PodcastData> records = new ArrayList<>();

	private long total;

	private long limit;

	private Podcasts() {
	}

	public List<PodcastData> getPodcasts() {
		return records;
	}

	public long getTotal() {
		return total;
	}

	public long getLimit() {
		return limit;
	}

	public void forEach(Consumer<PodcastData> consumer) {
		records.forEach(consumer);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("records", records)
				.append("total", total)
				.append("limit", limit)
				.toString();
	}
}
