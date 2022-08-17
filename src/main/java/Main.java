import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<Person> persons = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        URL urlJSON = Main.class.getClassLoader().getResource("sample.json");
        URL urlCSV = Main.class.getClassLoader().getResource("sample.csv");
        URL urlXML = Main.class.getClassLoader().getResource("sample.xml");

        List<Person> resultJSON = new ORM().transform(new File(urlJSON.toURI()), Person.class);
        List<Person> resultCSV = new ORM().transform(new File(urlCSV.toURI()), Person.class);
        List<Person> resultXML = new ORM().transform(new File(urlXML.toURI()), Person.class);
    }
}
