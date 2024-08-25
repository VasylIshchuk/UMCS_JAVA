//  Custom exception that will display a message if the file contains more than one person with the same first name and last name.
public class AmbiguousPersonException extends Exception {
    public AmbiguousPersonException(Person person) {
        super("Ambiguous person: " + person.getName() + ", born in: " + person.getDateBirth() +
                ", dead in: " + person.getDateDeath());
    }
}
