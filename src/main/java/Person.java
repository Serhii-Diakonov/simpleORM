import java.time.LocalDate;

public class Person {
    private String name;
    private String surname;
    private LocalDate birth;
    private String position;
    private double salary;

    public Person() {
    }

    public Person(String name, String surname, LocalDate birth, String position, double salary) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.position = position;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birth=" + birth +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }
}
