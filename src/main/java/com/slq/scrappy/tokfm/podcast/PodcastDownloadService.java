package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.TokFmRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static com.slq.scrappy.tokfm.JsonObjectMapper.createJsonObjectMapper;
import static org.apache.http.HttpStatus.SC_OK;
import static org.jsoup.Jsoup.connect;

public class PodcastDownloadService {

	private final HttpClient client;

	public PodcastDownloadService(HttpClient client) {
		this.client = client;
	}

	public Podcasts listPodcasts(String url) throws IOException {
		String json;
		try {
			json = connect(url)
					.ignoreContentType(true)
					.execute()
					.body();
		} catch (SocketTimeoutException exception) {
			System.out.println("Retrying: " + url);
			return listPodcasts(url);
		}

		return createJsonObjectMapper().readValue(json, Podcasts.class);
	}

	public HttpResponse executeRequest(TokFmRequest request) throws IOException {
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() != SC_OK) {
			System.err.println(response.getStatusLine().getReasonPhrase());
		}

		return response;
	}
}
