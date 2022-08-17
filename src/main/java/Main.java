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

        ORMInterface _interface = new ORM();

        List<Person> resultJSON = _interface.transform(new ORMInterface.StringInputSource(readFileToString(new File(urlJSON.toURI()))), Person.class);
        List<Person> resultCSV = _interface.transform(new ORMInterface.StringInputSource(readFileToString(new File(urlCSV.toURI()))), Person.class);
        List<Person> resultXML = _interface.transform(new ORMInterface.StringInputSource(readFileToString(new File(urlXML.toURI()))), Person.class);
    }

    private static String readFileToString(File file) {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                fileContent.append(buffer).append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent.toString();
    }
}
