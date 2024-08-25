import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Person implements Serializable {
    private String name;
    private LocalDate dateBirth;
    private LocalDate dateDeath;
    private List<Person> parents = new ArrayList<>();

    public String getName() {
        return name;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public LocalDate getDateDeath() {
        return dateDeath;
    }

    //    Adds a parent to the list of parents for the current person. This is necessary to establish connections between each person.
    public void addParent(Person parent) {
        this.parents.add(parent);
    }

    //    This is the main method that runs at the start to identify the people in the file and establish connections between them.
    public static List<Person> fromCsv(String path) {
        //    This is a list that contains all the people in the file.
        List<Person> peopleList = new ArrayList<>();
        //    This map is needed to connect these people based on the relationships they have with each other.
        //    The key is the person's name, and the value contains all the person's data, including the names of their parents.
        Map<String, PersonWithParentsNames> personWithParentsNamesMap = new HashMap<>();
        String csvLine;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            bufferedReader.readLine();
            //  We go through the entire file and execute this loop for each line.
            //  Where each line contains all the data about one person.
            while ((csvLine = bufferedReader.readLine()) != null) {
                //  Process one line.
                PersonWithParentsNames personWithParentsNames =
                        PersonWithParentsNames.fromCsvLine(csvLine);
                Person person = personWithParentsNames.getPerson();
                personWithParentsNamesMap.put(person.name, personWithParentsNames);

                //  Checks for exceptions.
                person.validateLifespan();
                person.validateAmbiguousPerson(peopleList);

                peopleList.add(person);
            }
            //  Establishes connections between parents and children.
            PersonWithParentsNames.linkRelatives(personWithParentsNamesMap);
            //  Checks for exceptions.
            try {
                for (Person person : peopleList)
                    person.validateParentingAge();
            } catch (ParentingAgeException e) {
                Scanner scanner = new Scanner(System.in);
                System.out.println(e.getMessage());
                while (true) {
                    System.out.print("Confirm this case 'Y' or reject 'N' : ");
                    String response = scanner.next();
                    if (response.equals("Y") || response.equals("y")) {
                        peopleList.remove(e.person);
                        break;
                    } else if (response.equals("N") || response.equals("n")) {
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NegativeLifespanException e) {
            System.err.println(e.getMessage());
        } catch (AmbiguousPersonException e) {
            System.err.println(e.getMessage());
        }
        return peopleList;
    }

    //  Creates a person object with the given data.
    public static Person fromCsvLine(String csvLine) {
        String[] dataPerson = csvLine.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Person person = new Person();
        person.name = dataPerson[0];
        person.dateBirth = person.dateBirth.parse(dataPerson[1], formatter);
        if (!dataPerson[2].isEmpty())
            person.dateDeath = person.dateDeath.parse(dataPerson[2], formatter);
        return person;
    }

    //  Throws an exception (AmbiguousPersonException) if the person's date of death precedes their date of birth.
    public void validateLifespan() throws NegativeLifespanException {
        if (this.dateDeath != null && this.dateDeath.isBefore(this.dateBirth)) {
            throw new NegativeLifespanException(this);
        }
    }

    //  Throws an exception (AmbiguousPersonException) if the file contains more than one person with the same first name and last name.
    public void validateAmbiguousPerson(List<Person> peopleList) throws AmbiguousPersonException {
        for (Person person : peopleList) {
            if (this.name.equals(person.name)) {
                throw new AmbiguousPersonException(this);
            }
        }
    }

    //  Throws an exception (ParentingAgeException) if the parent is younger than 15 years old or has died before the child was born.
    private void validateParentingAge() throws ParentingAgeException {
        for (Person parent : parents) {
            if (ChronoUnit.YEARS.between(parent.dateBirth,
                    (parent.dateDeath == null ? LocalDate.now() : parent.dateDeath)) < 15 ||
                    (parent.dateDeath != null && parent.dateDeath.isBefore(this.dateBirth))) {
                throw new ParentingAgeException(this, parent);
            }
        }
    }

    public static void toBinaryFile(List<Person> peopleList, String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(peopleList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Person> fromBinaryFile(String path) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (List<Person>) objectInputStream.readObject();
        }
    }

    //  A method that returns a string formatted according to PlantUML syntax, which means the data that will be included in the file.
    //  But with additional parameters.
    public String toPlantUmlWihParents(
            Function<String, String> postProcess, Predicate<Person> condition) {
        StringBuilder plantUml = new StringBuilder();
        Function<String, String> replaceWhitespace =
                str -> str.replaceAll(" ", "");

        plantUml.append("@startuml\n")
                .append("object ")
                .append(replaceWhitespace.apply(name));
        //  "postProcess" is an additional parameter to Uml that will be added only if the "condition" is done and .test returns true.
        //  This happens when the object satisfies the condition we provided.
        if (condition.test(this)) plantUml.append(" " + postProcess.apply(""));
        plantUml.append("\n");

        for (Person parent : parents) {
            plantUml.append("object ")
                    .append(replaceWhitespace.apply(parent.name) + "\n");
        }
        for (Person parent : parents) {
            plantUml.append(replaceWhitespace.apply(name))
                    .append(" --> ").append(replaceWhitespace.apply(parent.name) + "\n");
        }
        plantUml.append("@enduml");
        return String.valueOf(plantUml);
    }

    //    A method that returns a string formatted according to PlantUML syntax, which means the data that will be included in the file.
    public static String toPlantUmlPeople(List<Person> peopleList) {
        Set<String> object = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Function<String, String> replaceWhitespace =
                str -> str.replaceAll(" ", "");

        for (Person person : peopleList) {
            object.add("object " + replaceWhitespace.apply(person.name));
            for (Person parent : person.parents) {
                relations.add(replaceWhitespace.apply(person.name) +
                        " --> " + replaceWhitespace.apply(parent.name));
            }
        }
        //  For joining strings from collections (such as List, Set, etc.) with a specified delimiter.
        return String.format(Locale.ENGLISH, "@startuml\n%s\n%s\n@enduml",
                String.join("\n", object),
                String.join("\n", relations));
    }

    //  The method should return a list of people from the input list, restricted to those whose name contains the substring.
    public static List<Person> filterListBySubstring(List<Person> peopleList, String substring) {
        return peopleList.stream()
                .filter(person -> person.name.equals(substring))
                .collect(Collectors.toList());
    }

    public static List<Person> sortedListByDateBirth(List<Person> personList) {
        return personList.stream()
                .sorted(Comparator.comparing(person -> person.dateBirth))
                //(person1, person2) -> person1.dateBirth.compareTo(person2.dateBirth) sorted in increasing order
                .collect(Collectors.toList());
    }

    public static List<Person> sortedListByDateDeath(List<Person> personList) {
        return personList.stream()
                .filter(person -> person.dateDeath != null)
                .sorted((person1, person2) -> person2.dateDeath.compareTo(person1.dateDeath))//sorted in descending order
                .collect(Collectors.toList());
    }

    public static List<Person> sortedDeadPersonByAge(List<Person> personList) {
        return personList.stream()
                .filter(person -> person.dateDeath != null)
                .sorted(Comparator.comparing(
                        person -> ChronoUnit.YEARS.between(person.dateBirth, person.dateDeath)))
                .collect(Collectors.toList());
    }

    public static Person olderLivePerson(List<Person> personList) {
        return personList.stream()
                .filter(person -> person.dateDeath != null)
                .max(Comparator.comparing(person ->
                        (int) ChronoUnit.YEARS.between(person.dateBirth, person.dateDeath)))
                .orElseThrow(NoSuchElementException::new);
    }

    //  The format in which my object Person will be displayed.
    @Override
    public String toString() {
        return "Person { " +
                "name = \'" + name + '\'' +
                ", birthDate = " + dateBirth +
                ", deathDate = " + dateDeath +
                ", parents=" + parents +
                " }";
    }
}
