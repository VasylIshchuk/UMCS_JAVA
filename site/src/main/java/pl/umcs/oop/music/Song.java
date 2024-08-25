package pl.umcs.oop.music;

import pl.umcs.oop.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public record Song(String artist, String title, int duration) {

    public static class Persistence {
        // The read method retrieves a Song from the database using its unique index (id).
        // It returns an Optional<Song> to safely handle cases where the song may not be found.
        public static Optional<Song> read(int index) {
            //  It is used to represent a value that may or may not be present.
            //  In other words, an Optional object can either contain a non-null value
            //  (in which case it is considered present) or it can contain no value at all
            //  (in which case it is considered empty).
            String sql = "SELECT * FROM song WHERE id = ?";
            try {
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
                statement.setInt(1, index);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String artist = resultSet.getString(2);
                    String title = resultSet.getString(3);
                    int duration = resultSet.getInt(4);
                    return Optional.of(new Song(artist, title, duration));
                    //create Optional object that is present and contains the given non-null value
                }
                return Optional.empty();//create an empty Optional object
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
