import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class Main {
    public static void main(String[] args) {
        List<Person> peopleList = Person.fromCsv("family.csv");
        peopleList = Person.sortedDeadPersonByAge(peopleList);
        //for(Person human : peopleList) System.out.println(human);
        Person olderPerson = Person.olderLivePerson(peopleList);
        Person person = peopleList.get(5);//.get(0)
        //System.out.println(person);
        //System.out.println(olderPerson);
        PlantUMLRunner.setPathJarUml("plantuml-1.2024.4.jar");

        Function<String, String> color = str -> str + "#Lime";
        Predicate<Person> condition =
                man -> man.getName().equals(olderPerson.getName());
        String data = person.toPlantUmlWihParents(color, condition);
        PlantUMLRunner.generateDiagram(data, "uml", "condition");
    }
}