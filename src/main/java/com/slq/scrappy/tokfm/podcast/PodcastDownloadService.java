package com.slq.scrappy.tokfm.podcast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static org.jsoup.Jsoup.connect;

public class PodcastDownloadService {

	public Podcasts listPodcasts(String url) throws IOException {
		String json = "";
		try {
			json = connect(url)
					.ignoreContentType(true)
					.execute()
					.body();
		} catch (SocketTimeoutException exception) {
			System.out.println("Retrying: " + url);
			return listPodcasts(url);
		}

		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();

		return gson.fromJson(json, Podcasts.class);
	}
}
