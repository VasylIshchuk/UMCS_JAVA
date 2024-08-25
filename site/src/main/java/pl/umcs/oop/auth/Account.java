package pl.umcs.oop.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.umcs.oop.database.DatabaseConnection;

import javax.naming.AuthenticationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    protected final int id;
    protected final String username;

    public Account(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public static class Persistence {
        //  Initializes the database for accounts.
        public static void init() {
            try {
                String createSQLTable = "CREATE TABLE IF NOT EXISTS accounts( " +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "password TEXT NOT NULL)";
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(createSQLTable);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public static int register(String username, String password) {
            // Hash the password using BCrypt with a work factor of 12.
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            try {
                String insertSQL = "INSERT INTO accounts(username, password) VALUES (?, ?)";
                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(insertSQL);

                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                //  statement.getGeneratedKeys(): Returns a ResultSet containing the generated keys. In your case,
                //  this will be the primary key (probably the ID) that was automatically generated for the new record.
                if (resultSet.next())
                    return resultSet.getInt(1);
                    //  resultSet.getInt(1): Gets the value of the first column in the ResultSet, which is the generated key.
                else throw new SQLException();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public static Account authenticate(String username, String password) throws AuthenticationException {
            try {
                String sql = "SELECT id, username, password FROM accounts WHERE username = ?";

                PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
                statement.setString(1, username);

                // Execute the SQL statement. If it returns false, throw an exception indicating that the SELECT failed.
                if (!statement.execute()) throw new RuntimeException("SELECT failed");

                ResultSet result = statement.getResultSet();

                if (!result.next()) throw new AuthenticationException("No such user :(");

                String hashedPassword = result.getString(3);
                // Verify the provided password against the hashed password from the database.
                // BCrypt.verifyer().verify() checks if the provided password matches the stored hash.
                boolean okay = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword.toCharArray()).verified;

                if (!okay) throw new AuthenticationException("Wrong password :|");

                return new Account(
                        result.getInt(1),
                        result.getString(2)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
