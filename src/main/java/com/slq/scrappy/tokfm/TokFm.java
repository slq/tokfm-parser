package com.slq.scrappy.tokfm;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TokFm {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        System.out.println("Hello Tok FM!");

        String startUrl = "http://audycje.tokfm.pl/podcasts";
        String baseUrl = "http://audycje.tokfm.pl/podcasts?offset=%d";

        for (int offset = 0; offset < 1000; offset++) {
            String url = offset > 0 ? String.format(baseUrl, offset) : startUrl;

            String json = Jsoup.connect(url).ignoreContentType(true)
                    .execute().body();


            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            Podcasts podcasts = gson.fromJson(json, Podcasts.class);

            for (Podcast podcast : podcasts.getPodcasts()) {
                String n = String.format("%x", (int) Math.floor(System.currentTimeMillis() / 1000)).toUpperCase();

                String audioName = podcast.getPodcast_audio();
                String fileId = podcast.getPodcast_id();
                String message = "MwbJdy3jUC2xChua/" + audioName + n;
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = message.getBytes("UTF-8");
                byte[] digest = md5.digest(bytes);
                BigInteger bigInt = new BigInteger(1, digest);
                String mdp = bigInt.toString(16);
                while (mdp.length() < 32) {
                    mdp = "0" + mdp;
                }
                mdp = mdp.toUpperCase();

                String load = n + "." + mdp.substring(12, 16) + "." + mdp.substring(8, 12) + "." + mdp.substring(16, 20) + "." + mdp.substring(20, 24) + ".";
                Random random = new Random();
                String token = String.format(String.valueOf(random.nextInt(255)), 'x').toUpperCase() + "." + mdp.substring(0, 4) + "." + mdp.substring(4, 8) + "." + mdp.substring(28, 32) + "." + mdp.substring(24, 28) + ".";

                String uri = String.format("http://storage.tuba.fm/load_podcast/%s.mp3", fileId);
                String output = "aaa.mp3";
                String curl = String.format("curl " +
                        "--data \"pre=load\" " +
                        "--header \"X-Tuba:%s\" " +
                        "--header \"X-Tuba-Load:%s\" " +
                        "--header \"X-Tuba-Token:%s\" " +
                        "http://storage.tuba.fm/load_podcast/%s.mp3 " +
                        "-o \"%s\"", audioName, load, token, fileId, output);

                HttpClient client = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost(uri);
                request.addHeader("X-Tuba", audioName);
                request.addHeader("X-Tuba-Load", load);
                request.addHeader("X-Tuba-Token", token);
                HttpResponse response = client.execute(request);


                HttpEntity entity = response.getEntity();

                String filename = String.format("%s - %s - %s.mp3", podcast.getPodcast_emission_text(), podcast.getSeries_name(), podcast.getPodcast_name());
                String home = System.getProperty("user.home");
                Path path = Paths.get(home, "Downloads", "TokFM", filename);

                if (path.toFile().exists()) {
                    System.out.println(String.format("%s - Skipping", filename));
                    continue;
                }

                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    DownloadCountingOutputStream outputStream = new DownloadCountingOutputStream(new FileOutputStream(path.toFile()), filename);
                    outputStream.setListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DownloadCountingOutputStream stream = (DownloadCountingOutputStream) e.getSource();
                            double byteCount = stream.getByteCount() / 1024.0 / 1024.0;
                            String streamName = stream.getStreamName();
                            try {
                                System.out.write(String.format("\r%s : %.2f", streamName, byteCount).getBytes());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                    System.out.println();
                }
            }
        }
    }
}
