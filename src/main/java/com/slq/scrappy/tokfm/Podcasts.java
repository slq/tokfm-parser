package com.slq.scrappy.tokfm;

import java.util.ArrayList;
import java.util.Arrays;

public class Podcasts {

    private ArrayList<Podcast> records;

    public ArrayList<Podcast> getPodcasts() {
        return records;
    }

    @Override
    public String toString() {
        return "Podcasts{" +
                "records=" + records +
                '}';
    }
}
