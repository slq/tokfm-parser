package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.Podcast;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.format;

public class TokFmRequest extends HttpGet {

	private static final String MD5_PHRASE = "MwbJdy3jUC2xChua/";
	private static final int MD5_SIZE = 32;
	private static final int RADIX = 16;
	private static final int MILIS = 1000;

	private TokFmRequest(String uri) {
		super(uri);
	}

	public static TokFmRequest from(Podcast podcast) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String hexTime = currentTimeSecondsToHex();
		String audioName = podcast.getAudio();
		byte[] digest = digest(hexTime, audioName);
		BigInteger bigInt = new BigInteger(1, digest);
		String mdp = bigInt.toString(RADIX).toLowerCase();
		mdp = StringUtils.leftPad(mdp, MD5_SIZE, '0');
		String uri = format("http://storage.tuba.fm/podcast/%s/%s/%s", mdp, hexTime, audioName);
		return new TokFmRequest(uri);
	}

	private static String currentTimeSecondsToHex() {
		return format("%x", (int) Math.floor(System.currentTimeMillis() / MILIS)).toUpperCase();
	}

	private static byte[] digest(String n, String audioName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String message = MD5_PHRASE + audioName + n;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] bytes = message.getBytes("UTF-8");
		return md5.digest(bytes);
	}
}
