package com.slq.scrappy.tokfm.podcast;

import com.google.gson.annotations.SerializedName;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substring;

public class Podcast {

    @SerializedName("podcast_id")
    private String id;

    @SerializedName("podcast_name")
    private String name;

    @SerializedName("podcast_audio")
    private String audio;

    @SerializedName("series_name")
    private String seriesName;

    @SerializedName("podcast_emission_text")
    private String emissionText;

    private Podcast() {}


    public String getId() {
        return id;
    }

    public String getName() {
        return normalize(name);
    }

    public String getAudio() {
        return audio;
    }

    public String getSeriesName() {
        return normalize(seriesName);
    }

    public String getEmissionText() {
        return normalize(emissionText);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", audio='" + audio + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", emissionText='" + emissionText + '\'' +
                '}';
    }

    private static String normalize(String str) {
        return substring(replaceUnwantedCharacters(str).trim(), 0, 200);
    }

    private static String replaceUnwantedCharacters(String str) {
        return str.replaceAll("\\?", ".")
                .replaceAll("!", ".")
                .replaceAll(":", "")
                .replaceAll("\'\'", "\'")
                .replaceAll("\"", "\'")
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("„", "")
                .replaceAll("\\\\", "-")
                .replaceAll("/", "-")
                .replaceAll(">", ".")
                .replaceAll("<", ".");
    }

    public String getTargetFilename() {
        return format("%s - %s - %s.mp3", getEmissionText(), getSeriesName(), getName());
                //.replaceAll(".", ".");
    }
}
