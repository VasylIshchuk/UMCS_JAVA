import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonWithParentsNames {
    private Person person;
    List<String> parentsNames;
    //    Returns the field "person" contained within the object PersonWithParentsNames that invoked this method.
    public Person getPerson() {
        return person;
    }

    public PersonWithParentsNames(Person person, List<String> parentsNames) {
        this.person = person;
        this.parentsNames = parentsNames;
    }

    public static PersonWithParentsNames fromCsvLine(String csvLine) {
        //      limit < 0: The string will be split into all possible substrings, including empty strings.
        String[] dataPerson = csvLine.split(",", -1);
        //      Determines what kind of person it is.
        Person person = Person.fromCsvLine(csvLine);
        //      Determines who the parents are.
        List<String> parentsNames = new ArrayList<>();
        for (int i = 3; i <= 4; ++i) {
            if (!dataPerson[i].isEmpty()) {
                parentsNames.add(dataPerson[i]);
            }
        }
        return new PersonWithParentsNames(person, parentsNames);
    }

//    Establishes connections between parents and children.
//    We have a map where the keys are people's names, and the values are PersonWithParentsNames objects
//    containing information about their parents.
    public static void linkRelatives(Map<String, PersonWithParentsNames> map) {
        for (PersonWithParentsNames helperChild : map.values()) {
            for (String parentName : helperChild.parentsNames) {
                PersonWithParentsNames helperParent = map.get(parentName);
                Person parent = helperParent.getPerson();
                helperChild.getPerson().addParent(parent);
            }
        }
    }
//    for (PersonWithParentsNames helperChild : map.values()) {
//          The "map.values()" method returns a collection of all the "values" in the map, which are objects of type PersonWithParentsNames.
//          This loop iterates through each of these objects, referred to as "helperChild", meaning that we are processing each person in the map,
//          treating them as a "child" in this context.
//      for (String parentName : helperChild.parentsNames) {
//              "helperChild.parentsNames" is a list of parent names for the current "helperChild".
//              The method iterates through each parent name in this list(List<String> parentsNames).
//          PersonWithParentsNames helperParent = map.get(parentName);
//                  We use the parent’s name (parentName) to retrieve the PersonWithParentsNames object representing the parent from the map.
//                  This object(helperParent) contains the Person instance (who is the parent) and a list of the parent's parents.
//          Person parent = helperParent.getPerson();
//                  The getPerson() method retrieves the Person object from the PersonWithParentsNames object helperParent,
//                  which represents the parent.
//          helperChild.getPerson().addParent(parent);
//                  The getPerson() method returns the Person object representing the current child (helperChild).
//                  The addParent(parent) method of the Person class adds(the Person object "parent") a parent to the child's list of parents.
}
