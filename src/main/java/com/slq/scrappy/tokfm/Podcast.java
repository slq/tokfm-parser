package com.slq.scrappy.tokfm;

import org.apache.commons.lang3.StringUtils;

public class Podcast {
    private String podcast_id;
    private String podcast_name;
    private String podcast_audio;
    private String series_name;
    private String podcast_emission_text;

    public String getPodcast_id() {
        return podcast_id;
    }

    public String getPodcast_name() {
        return normalize(podcast_name);

    }

    public String getPodcast_audio() {
        return podcast_audio;
    }

    public String getSeries_name() {
        return normalize(series_name);
    }

    public String getPodcast_emission_text() {
        return normalize(podcast_emission_text);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "podcast_id='" + podcast_id + '\'' +
                ", podcast_name='" + podcast_name + '\'' +
                ", podcast_audio='" + podcast_audio + '\'' +
                ", series_name='" + series_name + '\'' +
                ", podcast_emission_text='" + podcast_emission_text + '\'' +
                '}';
    }

    private static String normalize(String str) {
        return StringUtils.substring(
                str.replaceAll("\\?", ".")
                .replaceAll("!", ".")
                .replaceAll(":", "")
                .replaceAll("\'\'", "\'")
                .replaceAll("\"", "\'")
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("„", "")
                .replaceAll("\\\\", "-")
                .replaceAll("/", "-")
                .trim(), 0, 200);
    }
}
