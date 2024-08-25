package pl.umcs.oop.music;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import pl.umcs.oop.database.DatabaseConnection;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    @BeforeAll
    public static void connectWithDatabase() {
        DatabaseConnection.connect("songs.db");
    }

    @AfterAll
    public static void disconnectWithDatabase() {
        DatabaseConnection.disconnect();
    }

    @Test
    public void testReadFromDatabaseThroughCorrectIndex() {
        int index = 6;
        Optional<Song> song = Song.Persistence.read(index);
        Song sqlSong = new Song("Pink Floyd", "Wish You Were Here", 334);
        assertEquals(song.get(), sqlSong);
    }

    @Test
    public void testReadFromDatabaseThroughIncorrectIndex() {
        int index = 50;
        Optional<Song> song = Song.Persistence.read(index);
        assertTrue(song.isEmpty());
    }

    private static Stream<Arguments> streamSongs() {
        return Stream.of(
                Arguments.arguments(1, "The Beatles", "Hey Jude", 431),
                Arguments.arguments(10, "The Who", "My Generation", 198),
                Arguments.arguments(36, "Pink Floyd", "Comfortably Numb", 382)
        );
//        Create method from arguments that we will then use in the test
    }

    @ParameterizedTest
//   This feature enables us to execute a single test method multiple times with different parameters.
//   In order to use JUnit 5 parameterized tests, we need to import the junit-jupiter-params artifact in pom.xml.
    @MethodSource("streamSongs")
    public void testMethodStreamSongs(int id, String artist, String title, int duration) {
        Song song = new Song(artist, title, duration);
        Optional<Song> sqlSong = Song.Persistence.read(id);
        assertEquals(song, sqlSong.get());
    }

    @ParameterizedTest
    @CsvFileSource(files = "songs.csv", numLinesToSkip = 1)
//if write (.) in brackets you can see all parameters that are available
    public void testCsvFile(int id, String artist, String title, int duration) {
        Song song = new Song(artist, title, duration);
        Optional<Song> sqlSong = Song.Persistence.read(id);
        assertEquals(song, sqlSong.get());
    }


}