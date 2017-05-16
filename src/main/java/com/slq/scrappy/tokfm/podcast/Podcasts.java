package com.slq.scrappy.tokfm.podcast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Podcasts {

	private List<Podcast> records = new ArrayList<>();

	private Podcasts() {}

	public List<Podcast> getPodcasts() {
		return records;
	}

	public Stream<Podcast> forEach() {
		return records.stream();
	}

	@Override
	public String toString() {
		return "Podcasts{" +
				"records=" + records +
				'}';
	}
}
