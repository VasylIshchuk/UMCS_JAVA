//  Custom exception that will display a message if the person's date of death precedes their date of birth.
public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(Person person) {
        super("Person: " + person.getName() + ", born in: " + person.getDateBirth() +
                ", died in: " + person.getDateDeath() +
                ". Died before Birthdate!!!");
    }

}
