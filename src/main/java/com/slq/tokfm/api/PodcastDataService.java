package com.slq.tokfm.api;

import java.io.OutputStream;

public interface PodcastDataService {
	OutputStream getById(String podcastId);
}
