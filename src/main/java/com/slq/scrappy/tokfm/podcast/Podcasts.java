package com.slq.scrappy.tokfm.podcast;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Podcasts {

    private ArrayList<Podcast> records;

    private Podcasts() {}

    public ArrayList<Podcast> getPodcasts() {
        return records;
    }

    public Stream<Podcast> forEach(){
        return records.stream();
    }

    @Override
    public String toString() {
        return "Podcasts{" +
                "records=" + records +
                '}';
    }
}
