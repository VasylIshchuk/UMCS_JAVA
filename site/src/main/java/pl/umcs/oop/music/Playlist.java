package pl.umcs.oop.music;

import java.util.ArrayList;

public class Playlist extends ArrayList<Song> {
    //    A method atSecond that takes an integer representing the number of seconds and returns the Song object
    //    that is playing after that many seconds of playlist playback.
    public Song atSecond(int seconds) {
        if (seconds < 0)
            throw new IndexOutOfBoundsException("Negative time were given!");
        for (Song song : this) {
            seconds = seconds - song.duration();
            if (seconds <= 0) return song;
        }
        throw new IndexOutOfBoundsException("Time out of playlist");
    }
}
