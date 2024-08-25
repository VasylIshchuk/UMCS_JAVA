package pl.umcs.oop.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    @Test //This is annotation
    public void testIfPlayListIsEmpty() {
        Playlist playlist = new Playlist();
        assertTrue(playlist.isEmpty());
    }

    @Test
    public void testIfPlaylistAddSongs() {
        Playlist playlist = new Playlist();
        Song song = new Song("Pink Floyd", "Wish You Were Here", 334);
        playlist.add(song);
        assertEquals(playlist.size(), 1);
    }

    @Test
    public void testIfTheSameSongHasAfterAdding() {
        Playlist playlist = new Playlist();
        Song song = new Song("Pink Floyd", "Wish You Were Here", 334);
        playlist.add(song);
        assertEquals(playlist.get(0), song);
    }

    @Test
    public void testAtSeconds() {
        Playlist playlist = new Playlist();
        Song song1 = new Song("Pink Floyd", "Wish You Were Here", 334);
        Song song2 = new Song("John Lennon", "Imagine", 183);
        Song song3 = new Song("Pink Floyd", "Comfortably Numb", 382);
        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);
        Song currentSong = playlist.atSecond(550);
        assertEquals(song3, currentSong);
    }

    @Test
    public void testIfAtSecondsThrowException() {
        Playlist playlist = new Playlist();
        Song song1 = new Song("Pink Floyd", "Wish You Were Here", 334);
        Song song2 = new Song("John Lennon", "Imagine", 183);
        Song song3 = new Song("Pink Floyd", "Comfortably Numb", 382);
        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);
        assertThrows(IndexOutOfBoundsException.class,
                () -> playlist.atSecond(1000));
    }

    @Test
    public void testIfAtSecondsThrowExceptionOutOfPlaylist() {
        Playlist playlist = new Playlist();
        Song song1 = new Song("Pink Floyd", "Wish You Were Here", 334);
        Song song2 = new Song("John Lennon", "Imagine", 183);
        Song song3 = new Song("Pink Floyd", "Comfortably Numb", 382);
        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);
        IndexOutOfBoundsException thrown = assertThrows(IndexOutOfBoundsException.class,
                () -> playlist.atSecond(1000));
        assertEquals(thrown.getMessage(), "Time out of playlist");
//        assertEquals(assertThrows(IndexOutOfBoundsException.class,
//                () -> playlist.atSecond(1000)).getMessage(),"Time out of playlist");
    }

    @Test
    public void testIfAtSecondsThrowExceptionNegativeTime() {
        Playlist playlist = new Playlist();
        Song song1 = new Song("Pink Floyd", "Wish You Were Here", 334);
        Song song2 = new Song("John Lennon", "Imagine", 183);
        Song song3 = new Song("Pink Floyd", "Comfortably Numb", 382);
        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);
        IndexOutOfBoundsException thrown = assertThrows(IndexOutOfBoundsException.class,
                () -> playlist.atSecond(-2));
        assertEquals(thrown.getMessage(), "Negative time were given!");
//        assertEquals(assertThrows(IndexOutOfBoundsException.class,
//                () -> playlist.atSecond(-100)).getMessage(),"Negative time were given!");
    }

}