package pl.umcs.oop.music;

import pl.umcs.oop.auth.Account;
import pl.umcs.oop.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class ListenerAccount extends Account {
    public ListenerAccount(int id, String username) {
        super(id, username);
    }
    //  Initializes the database for listener accounts.
    public static void init() {
        try {
            String createSQLTable = "CREATE TABLE IF NOT EXISTS accounts( " +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "credit INTEGER," +
                    "playlist TEXT)";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(createSQLTable);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCredit(int credit) {
        String sql = "UPDATE accounts SET credit=? WHERE id=?";
        try {
            PreparedStatement statement = DatabaseConnection.getConnection()
                    .prepareStatement(sql);
            statement.setInt(1, credit);
            statement.setInt(2, this.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCredit() {
        String sql = "SELECT * FROM accounts WHERE id=?";
        try {
            PreparedStatement statement = DatabaseConnection.getConnection()
                    .prepareStatement(sql);
            statement.setInt(1, this.id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(4);
            //  Returns:
            //      * the column value;
            //      * if the value is SQL NULL,the value returned is 0.
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSong(Song song) {
        String sql = "UPDATE accounts SET playlist=? WHERE id=?";
        try {
            PreparedStatement statement = DatabaseConnection.getConnection()
                    .prepareStatement(sql);
            statement.setString(1, String.format(Locale.ENGLISH,
                    "{%s,%s,%s} ", song.artist(), song.title(), song.duration()));
            statement.setInt(2, this.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRecord() {
        String sql = "DELETE FROM accounts  WHERE id=?";
        try {
            PreparedStatement statement = DatabaseConnection.getConnection()
                    .prepareStatement(sql);
            statement.setInt(1, this.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void buySong(int idSong) throws NotEnoughCreditsException {
        try {
            // Retrieve the Song object from the database using the song ID.
            Song song = Song.Persistence.read(idSong).get();

            // Prepare a SQL query to check if the song is already in the user's playlist.
            String sqlAccount = "SELECT * FROM accounts WHERE id=? AND playlist=?";
            PreparedStatement statementAccount = DatabaseConnection.getConnection()
                    .prepareStatement(sqlAccount);
            statementAccount.setInt(1, this.id);
            statementAccount.setString(2, String.format(Locale.ENGLISH,
                    "{%s,%s,%s} ", song.artist(), song.title(), song.duration()));

            // Execute the query to check if the song is already in the playlist.
            ResultSet resultSet2 = statementAccount.executeQuery();

            // If the song is not already in the playlist (no matching record found):
            if (!resultSet2.next()) {
                // Check if the user has enough credits to buy the song.
                if (this.getCredit() <= 0)
                    throw new NotEnoughCreditsException("Not enough credits");

                // Prepare a SQL query to update the user's playlist and decrement their credits.
                String sql = "UPDATE accounts SET playlist=? ,credit = credit-1 WHERE id=?";
                PreparedStatement statement = DatabaseConnection.getConnection()
                        .prepareStatement(sql);

                statement.setString(1, String.format(Locale.ENGLISH,
                        "%s,%s,%s", song.artist(), song.title(), song.duration()));
                statement.setInt(2, this.id);

                // Execute the update to add the song to the playlist and reduce the user's credits by 1.
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Playlist createPlaylist(List<Integer> listIdSongs) throws NotEnoughCreditsException {
        Playlist playlist = new Playlist();
        for (Integer idSong : listIdSongs) {
            this.buySong(idSong);
            // Add the purchased song to the playlist.
            playlist.add(Song.Persistence.read(idSong).get());
        }
        return playlist;
    }
}
