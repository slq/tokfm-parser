package com.slq.scrappy.tokfm;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.substring;
import static org.jsoup.Jsoup.connect;

public class TokFm {

    private static final String MD5_PHRASE = "MwbJdy3jUC2xChua/";
    private static final String LIST_OPTION = "l";
    private static final String SKIP_EXISTING_OPTION = "s";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption(LIST_OPTION, false, "list podcasts");
        options.addOption(SKIP_EXISTING_OPTION, false, "skip existing podcasts");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(LIST_OPTION)) {
            list();
        } else{
            boolean skipOption = cmd.hasOption(SKIP_EXISTING_OPTION);
            download(skipOption);
        }
    }

    private static void list() throws IOException {
        String startUrl = "http://audycje.tokfm.pl/podcasts?offset=%d";

        for (int offset = 0; offset < 100000; offset++) {
            String url = String.format(startUrl, offset);

            Podcasts podcasts = fetchPodcasts(url);

            for (Podcast podcast : podcasts.getPodcasts()) {

                String filename = String.format("%s. %s - %s - %s.mp3", podcast.getPodcast_id(), podcast.getPodcast_emission_text(), podcast.getSeries_name(), podcast.getPodcast_name());

                System.out.println(filename);
            }
        }
    }

    private static void download(boolean skipOption) throws IOException, NoSuchAlgorithmException {
        //        String startUrl = "http://audycje.tokfm.pl/podcasts";
        String startUrl = "http://audycje.tokfm.pl/podcasts?offset=%d";
//          String startUrl = "http://audycje.tokfm.pl/podcasts?offset=%d&series_id=11";


        for (int offset = 0; offset < 100000; offset++) {
            String url = String.format(startUrl, offset);

            Podcasts podcasts = fetchPodcasts(url);

            for (Podcast podcast : podcasts.getPodcasts()) {
                String hexTime = currentTimeSecondsToHex();
                String audioName = podcast.getPodcast_audio();

                byte[] digest = digest(hexTime, audioName);
                BigInteger bigInt = new BigInteger(1, digest);

                String mdp = bigInt.toString(16).toUpperCase();
                // not needed?
                mdp = StringUtils.leftPad(mdp, 32, '0');
                String load = hexTime + "." + mdp.substring(12, 16) + "." + mdp.substring(8, 12) + "." + mdp.substring(16, 20) + "." + mdp.substring(20, 24) + ".";

                Random random = new Random();
                String token = String.format(String.valueOf(random.nextInt(255)), 'x').toUpperCase() + "." + mdp.substring(0, 4) + "." + mdp.substring(4, 8) + "." + mdp.substring(28, 32) + "." + mdp.substring(24, 28) + ".";

                String fileId = podcast.getPodcast_id();
                String uri = String.format("http://storage.tuba.fm/load_podcast/%s.mp3", fileId);

                HttpPost request = new HttpPost(uri);
                request.addHeader("X-Tuba", audioName);
                request.addHeader("X-Tuba-Load", load);
                request.addHeader("X-Tuba-Token", token);

                HttpClient client = HttpClientBuilder.create().build();
                HttpResponse response = client.execute(request);


                HttpEntity entity = response.getEntity();

                String filename = String.format("%s - %s - %s.mp3", podcast.getPodcast_emission_text(), podcast.getSeries_name(), podcast.getPodcast_name());
                String home = System.getProperty("user.home");
                Path path = Paths.get(home, "Downloads", "TokFM", filename);

                if (path.toFile().exists()) {
                    String shortFilename = substring(filename, 0, 150);
                    if(skipOption) {
                        System.out.println(String.format("%-150s : Skipping", shortFilename));
                        continue;
                    } else {
                        System.out.println(String.format("%-150s : Already exists. Exiting...", shortFilename));
                        return;
                    }
                }

                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    DownloadCountingOutputStream outputStream = new DownloadCountingOutputStream(path);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                    System.out.println();
                }
            }
        }
    }

    private static String currentTimeSecondsToHex() {
        return String.format("%x", (int) Math.floor(System.currentTimeMillis() / 1000)).toUpperCase();
    }

    private static byte[] digest(String n, String audioName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String message = MD5_PHRASE + audioName + n;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = message.getBytes("UTF-8");
        return md5.digest(bytes);
    }

    private static Podcasts fetchPodcasts(String url) throws IOException {
        String json = connect(url)
                .ignoreContentType(true)
                .execute()
                .body();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.fromJson(json, Podcasts.class);
    }
}
