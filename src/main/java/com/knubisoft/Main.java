package com.knubisoft;

import com.knubisoft.entity.Person;
import com.knubisoft.entity.Person2;
import com.knubisoft.rwsource.DataReadWriteSource;
import com.knubisoft.rwsource.impl.ConnectionReadWriteSource;
import com.knubisoft.rwsource.impl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {

    public static final String JSON_SRC = "sample.json";
    public static final String CSV_SRC = "sample.csv";
    public static final String XML_SRC = "sample.xml";

    public static void main(String[] args) throws Exception {
        withConnection(conn -> {
            process(conn);
            return null;
        });
    }

    private static void withConnection(Function<Connection, Void> function) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            try (Statement stmt1 = c.createStatement();
                 Statement stmt2 = c.createStatement()) {
                stmt1.executeUpdate("CREATE TABLE IF NOT EXISTS person" +
                        "(id INTEGER not null," +
                        "name varchar(255)," +
                        "position varchar(255)," +
                        "age integer," +
                        "primary key (id))");
                stmt2.executeUpdate("CREATE TABLE IF NOT EXISTS person_short" +
                        "(id INTEGER not null," +
                        "name varchar(255)," +
                        "age integer," +
                        "primary key (id))");
                stmt1.executeUpdate("DELETE from person");
                stmt2.executeUpdate("DELETE from person_short");
                for (int i = 0; i < 10; i++) {
                    stmt1.executeUpdate("insert into person (name, position, age) values ('GIBBY', 'Officer', 26)");
                    stmt2.executeUpdate("insert into person_short (name, age) values ('CARL', 33)");
                }
            }
            function.apply(c);
        }
    }

    @SneakyThrows
    private static void process(Connection connection) {
//        URL url = Main.class.getClassLoader().getResource(CSV_SRC);
        ORMInterface orm = new ORM();
//        List<Person> res1;
//        List<Person2> res2;
//
//        res1 = orm.readAll(new FileReadWriteSource(new File(url.toURI())), Person.class);
//        res1.add(new Person("BILLY", BigInteger.ONE, BigInteger.TEN, "CEO", LocalDate.now(), 0f));
//        orm.writeAll(new FileReadWriteSource(new File("src/main/resources/sample_2.csv")), res1);

//        DataReadWriteSource<ResultSet> rw = new ConnectionReadWriteSource(connection);
//        res1 = orm.readAll(rw, Person.class);
//        res1.add(new Person("BILLY", BigInteger.ONE, BigInteger.TEN, "CEO", LocalDate.now(), 0f));
//        res2 = orm.readAll(rw, Person2.class);
//        orm.writeAll(rw, res1);

        orm.readAll(new FileReadWriteSource(new File("src/main/resources/sample.pdf")), RowStructure.class);
    }
}
