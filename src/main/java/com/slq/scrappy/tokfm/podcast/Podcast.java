package com.slq.scrappy.tokfm.podcast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substring;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Podcast {

	public static final int NAME_SIZE_LIMIT = 200;
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

	private Podcast() {
	}

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
		return new ToStringBuilder(this)
				.append("id", id)
				.append("name", name)
				.append("audio", audio)
				.append("seriesName", seriesName)
				.append("emissionText", emissionText)
				.toString();
	}

	private static String normalize(String str) {
		return substring(replaceUnwantedCharacters(str).trim(), 0, NAME_SIZE_LIMIT);
	}

	private static String replaceUnwantedCharacters(String str) {
		String fixed = replaceButNotLast(str, "!", ".");
		fixed = replaceButNotLast(fixed, "?", ".");
		return fixed.replaceAll(":", "")
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
		String result = str;
		if (!result.contains(from)) {
			return result;
		}

		if (result.lastIndexOf(from) == result.length() - 1) {
			result = result.substring(0, result.length() - 1);
		}

		return result.replaceAll(Pattern.quote(from), to);
	}

	public String getTargetFilename() {
		return format("%s - %s - %s.mp3", getEmissionText(), getSeriesName(), getName());
		//.replaceAll(".", ".");
	}
}
