package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.DownloadCountingOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class ResponseProcessor {

	public void process(HttpResponse response, Path targetPath) throws IOException {
		HttpEntity entity = response.getEntity();
		downloadRequestedFile(from(entity), to(targetPath));
	}

	private DownloadCountingOutputStream to(Path targetPath) throws FileNotFoundException {
		return new DownloadCountingOutputStream(targetPath);
	}

	private InputStream from(HttpEntity entity) throws IOException {
		return entity.getContent();
	}

	private void downloadRequestedFile(InputStream inputStream, OutputStream outputStream) throws IOException {
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();
		System.out.println();
	}
}
