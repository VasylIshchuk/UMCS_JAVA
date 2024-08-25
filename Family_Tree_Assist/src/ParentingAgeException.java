//  Custom exception that will display a message if the parent is younger than 15 years old or has died before the child was born.
public class ParentingAgeException extends Exception {
    public Person person;

    public ParentingAgeException(Person person, Person parent) {
        super(String.format("It is hard to imagine that %s could be parent to %s.",
                person.getName(), parent.getName()));
        this.person = person;
    }
}
