package com.slq.tokfm.api;

import static java.lang.String.format;

public interface Podcast {
    String getId();

    String getName();

    String getAudio();

    String getSeriesName();

    String getEmissionText();

    default String getTargetFilename() {
        return format("%s - %s - %s.mp3", getEmissionText(), getSeriesName(), getName());
    }
}
