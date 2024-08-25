package pl.umcs.oop;

import pl.umcs.oop.auth.Account;
import pl.umcs.oop.database.DatabaseConnection;

import javax.naming.AuthenticationException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.connect("test.db");
        Account.Persistence.init();
        //  Account.Persistenmusicce.register("notch", "verysecurepassword");
        try {
            Account notch = Account.Persistence.authenticate("notch", "verysecurepassword");
            System.out.println(notch);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
        DatabaseConnection.disconnect();
    }
}


