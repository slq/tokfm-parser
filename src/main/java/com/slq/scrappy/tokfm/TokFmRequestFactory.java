package com.slq.scrappy.tokfm;

import com.slq.scrappy.tokfm.podcast.Podcast;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class TokFmRequestFactory {
    public static TokFmRequest create(Podcast podcast) {
        try {
            return TokFmRequest.from(podcast);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
