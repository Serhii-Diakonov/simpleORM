import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<Person> persons = new ArrayList<>();

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/person.csv"))) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                parseString(buffer);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        persons.forEach(System.out::println);
    }

    private static void parseString(String str) {
        String[] array = str.split(",");
        String name = array[0];
        String surname = array[1];
        LocalDate birth;
        try {
            String[] dateArray = array[2].split("\\.");
            birth = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]));
//            birth = LocalDate.parse(array[2]);
        } catch (/*DateTimeParseException e*/ NumberFormatException e) {
            e.printStackTrace();
            birth = null;
        }
        String pos = array[3];
        Double salary;
        try {
            salary = Double.parseDouble(array[4]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            salary = (double) 0;
        }
        Person p = new Person(name, surname, birth, pos, salary);
        persons.add(p);
    }
}
