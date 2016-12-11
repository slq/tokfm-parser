package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.Podcast;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class TokFmRequest extends HttpPost {

    private static final String MD5_PHRASE = "MwbJdy3jUC2xChua/";

    private TokFmRequest(String uri) {
        super(uri);
    }

    public static TokFmRequest from(Podcast podcast) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String hexTime = currentTimeSecondsToHex();
        String audioName = podcast.getAudio();

        byte[] digest = digest(hexTime, audioName);
        BigInteger bigInt = new BigInteger(1, digest);

        String mdp = bigInt.toString(16).toUpperCase();
        // not needed?
        mdp = StringUtils.leftPad(mdp, 32, '0');
        String load = hexTime + "." + mdp.substring(12, 16) + "." + mdp.substring(8, 12) + "." + mdp.substring(16, 20) + "." + mdp.substring(20, 24) + ".";

        Random random = new Random();
        String token = format(valueOf(random.nextInt(255)), 'x').toUpperCase() + "." + mdp.substring(0, 4) + "." + mdp.substring(4, 8) + "." + mdp.substring(28, 32) + "." + mdp.substring(24, 28) + ".";

        String fileId = podcast.getId();
        String uri = format("http://storage.tuba.fm/load_podcast/%s.mp3", fileId);

        TokFmRequest request = new TokFmRequest(uri);
        request.addHeader("X-Tuba", audioName);
        request.addHeader("X-Tuba-Load", load);
        request.addHeader("X-Tuba-Token", token);
        return request;
    }

    private static String currentTimeSecondsToHex() {
        return format("%x", (int) Math.floor(System.currentTimeMillis() / 1000)).toUpperCase();
    }

    private static byte[] digest(String n, String audioName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String message = MD5_PHRASE + audioName + n;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = message.getBytes("UTF-8");
        return md5.digest(bytes);
    }
}
