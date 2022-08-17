package com.knubisoft;

import com.knubisoft.entity.Person;
import com.knubisoft.rwsource.impl.ConnectionReadWriteSource;
import com.knubisoft.rwsource.DataReadWriteSource;

import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {

    public static final String JSON_SRC = "sample.json";
    public static final String JSON_CSV = "sample.csv";
    public static final String JSON_XML = "sample.xml";

    static List<Person> persons = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        /*URL urlJSON = com.knubisoft.Main.class.getClassLoader().getResource("sample.json");
        URL urlCSV = com.knubisoft.Main.class.getClassLoader().getResource("sample.csv");
        URL urlXML = com.knubisoft.Main.class.getClassLoader().getResource("sample.xml");

        com.knubisoft.ORMInterface _interface = new com.knubisoft.ORM();

        List<com.knubisoft.entity.Person> resultJSON = _interface.transform(new com.knubisoft.ORMInterface.StringInputSource(readFileToString(new File(urlJSON.toURI()))), com.knubisoft.entity.Person.class);
        List<com.knubisoft.entity.Person> resultCSV = _interface.transform(new com.knubisoft.ORMInterface.StringInputSource(readFileToString(new File(urlCSV.toURI()))), com.knubisoft.entity.Person.class);
        List<com.knubisoft.entity.Person> resultXML = _interface.transform(new com.knubisoft.ORMInterface.StringInputSource(readFileToString(new File(urlXML.toURI()))), com.knubisoft.entity.Person.class);
   */

        withConnection(conn -> {
            process(conn);
            return null;
        });
    }

    private static void withConnection(Function<Connection, Void> function) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            try (Statement stmt = c.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXIST person" +
                        "(id INTEGER not null," +
                        "name varchar(255)" +
                        "position varchar(255)" +
                        "age integer" +
                        "primary key (id))");

                stmt.executeUpdate("DELETE from person");
                for (int i = 0; i < 10; i++) {
                    stmt.executeUpdate("insert into person (name, position, age) values ('GIBBY', 'Officer', 26)");
                }
            }
            function.apply(c);
        }
    }

    private static void process(Connection connection) {
        URL url = Main.class.getClassLoader().getResource(JSON_SRC);
        ORMInterface orm = new ORM();
        List<Person> res;

//        res = orm.readAll(new com.knubisoft.rwsource.impl.FileReadWriteSource(new File(url.toURI())), com.knubisoft.entity.Person.class);
//        res.add(new com.knubisoft.entity.Person("BILLY", BigInteger.ONE, BigInteger.TEN, "CEO", LocalDate.now(), 0f));
//        orm.writeAll(new com.knubisoft.rwsource.impl.FileReadWriteSource(new File(url.toURI())), res);
        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection, "person");
        res = orm.readAll(rw, Person.class);
        res.add(new Person("BILLY", BigInteger.ONE, BigInteger.TEN, "CEO", LocalDate.now(), 0f));
//        orm.writeAll(rw, res);
    }
}
