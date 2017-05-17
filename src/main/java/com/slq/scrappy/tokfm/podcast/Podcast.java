package com.slq.scrappy.tokfm.podcast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.regex.Pattern;


import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substring;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Podcast {

    @JsonProperty("podcast_id")
    private String id;

    @JsonProperty("podcast_name")
    private String name;

    @JsonProperty("podcast_audio")
    private String audio;

    @JsonProperty("series_name")
    private String seriesName;

    @JsonProperty("podcast_emission_text")
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
        str = replaceButNotLast(str, "!", ".");
        str = replaceButNotLast(str, "?", ".");
        return str.replaceAll(":", "")
                .replaceAll("\'\'", "\'")
                .replaceAll("\"", "\'")
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("„", "")
                .replaceAll("\\\\", "-")
                .replaceAll("/", "-")
                .replaceAll(">", ".")
                .replaceAll("<", ".")
                .replaceAll("\\|", ".");
    }

    private static String replaceButNotLast(String str, String from, String to) {
        if(!str.contains(from)){
            return str;
        }

        if(str.lastIndexOf(from) == str.length()-1){
            str = str.substring(0, str.length()-1);
        }

        return str.replaceAll(Pattern.quote(from), to);
    }

    public String getTargetFilename() {
        return format("%s - %s - %s.mp3", getEmissionText(), getSeriesName(), getName());
                //.replaceAll(".", ".");
    }
}
