package pl.umcs.oop.music;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.umcs.oop.auth.Account;
import pl.umcs.oop.database.DatabaseConnection;

import javax.naming.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListenerAccountTest {
    @BeforeAll
    public static void connectWithDatabase() {
        DatabaseConnection.connect("songs.db");
        ListenerAccount.init();
    }

    @AfterAll
    public static void disconnectWithDatabase() {
        DatabaseConnection.disconnect();
    }

    @Test
    public void testRegisterAccount() {
        int id = Account.Persistence.register(
                "Lord universe", "5d98gvb9d");
        Account account = new ListenerAccount(id, "Lord universe");
        assertEquals(account.getId(), id);
    }

    @Test
    public void testAuthenticateAccount() throws AuthenticationException {
        Account listenerAccount = new ListenerAccount(1, "Wild beast");
        Account account = Account.Persistence.authenticate(
                "Wild beast", "nj430sp4");
        assertEquals(listenerAccount.getId(), account.getId());
    }

    @Test
    public void testMethodGetCreditEmpty() {
        ListenerAccount listenerAccount = new ListenerAccount(
                3, "Lord universe");
        int credit = listenerAccount.getCredit();
        assertEquals(0, credit);
    }

    @Test
    public void testMethodAddCredit() {
        ListenerAccount listenerAccount = new ListenerAccount(
                2, "Classical spice");
        listenerAccount.addCredit(6);
        assertEquals(6, listenerAccount.getCredit());
    }

    @Test
    public void testMethodBuySongWhenSongIsInAccount() throws NotEnoughCreditsException {
        int id = 9;//Account.Persistence.register("Super Frank", "jks56sbk23l");
        ListenerAccount listenerAccount = new ListenerAccount(id, "Super Frank");
        listenerAccount.addCredit(4);
        Song song = new Song("Elvis Presley", "Suspicious Minds", 262);
        listenerAccount.addSong(song);
        listenerAccount.buySong(9);
        assertEquals(4, listenerAccount.getCredit());
    }

    @Test
    public void testMethodBuySongWhenNOSongInAccount() throws NotEnoughCreditsException {
        int id = Account.Persistence.register("King octopus", "jks56sbk23l");
        ListenerAccount listenerAccount = new ListenerAccount(id, "King octopus");
        listenerAccount.addCredit(4);
        listenerAccount.buySong(9);
        assertEquals(3, listenerAccount.getCredit());
    }

    @Test
    public void testMethodBuySongWhenNOSongInAccountAndCreditNull() {
        int id = Account.Persistence.register("Octavian", "sk23ni492");
        ListenerAccount listenerAccount = new ListenerAccount(id, "Octavian");
        assertThrows(NotEnoughCreditsException.class, () -> listenerAccount.buySong(9));
    }

    @Test
    public void testCreatePlaylist() throws NotEnoughCreditsException {
        Song song1 = new Song("James Brown", "I Got You (I Feel Good)", 167);
        Song song2 = new Song("The Who", "My Generation", 198);
        Song song3 = new Song("The Rolling Stones", "Paint It Black", 224);
        Song song4 = new Song("The Beach Boys", "Good Vibrations", 215);
        Playlist manuallyPlaylist = new Playlist();
        manuallyPlaylist.add(song1);
        manuallyPlaylist.add(song2);
        manuallyPlaylist.add(song3);
        manuallyPlaylist.add(song4);

        int id = Account.Persistence.register("Darling", "love42");
        ListenerAccount listenerAccount = new ListenerAccount(id, "darling");
        listenerAccount.addCredit(10);

        List<Integer> listIdSongs = new ArrayList<>();
        listIdSongs.add(39);
        listIdSongs.add(10);
        listIdSongs.add(24);
        listIdSongs.add(8);

        Playlist playlist = listenerAccount.createPlaylist(listIdSongs);
        assertEquals(manuallyPlaylist, playlist);
    }
}