import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<Person> persons = new ArrayList<>();

    public static void main(String[] args) {

        File csvFile = new File("src/main/resources/sample.csv");
        File jsonFile = new File("src/main/resources/sample.json");
        File xmlFile = new File("src/main/resources/sample.xml");

        List<Person> personList = DataProccesor.transform(csvFile, Person.class);
        for (Person p: personList) {
            System.out.println(p);
        }
        System.out.println();

        personList = DataProccesor.transform(jsonFile, Person.class);
        for (Person p: personList) {
            System.out.println(p);
        }
        System.out.println();

        personList = DataProccesor.transform(xmlFile, Person.class);
        for (Person p: personList) {
            System.out.println(p);
        }
    }
}
