package com.slq.scrappy.tokfm.podcast;

import com.slq.scrappy.tokfm.podcast.Podcast;
import com.slq.scrappy.tokfm.podcast.PodcastInterceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.apache.commons.lang3.StringUtils.isBlank;

public class FilenameMatchingPodcastInterceptor implements PodcastInterceptor {
    private String pattern;

    public FilenameMatchingPodcastInterceptor(String pattern) {
        this.pattern = pattern;
    }

    private boolean matching(String matchPattern, String text) {
        if (isBlank(matchPattern)) {
            return true;
        }

        Pattern pattern = Pattern.compile(matchPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            System.out.println("Matched: " + matcher.group());
            return true;
        }
        return false;
    }

    @Override
    public void process(Podcast podcast) {
        if (!matching(pattern, podcast.getTargetFilename())) {
            throw new IllegalArgumentException("Pattern not matching");
        }
    }
}
