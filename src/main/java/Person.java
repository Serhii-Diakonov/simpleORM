import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Person {
    private String name;
//    private String surname;

    private LocalDate dateOfBirth;
    private int age;
    private String position;
    private double salary;
    private Float xxx;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Float getXxx() {
        return xxx;
    }

    public void setXxx(Float xxx) {
        this.xxx = xxx;
    }

    public Person() {
    }

    public Person(String name, LocalDate birth, int age, String position, double salary, Float xxx) {
        this.name = name;
        this.dateOfBirth = birth;
        this.age = age;
        this.position = position;
        this.salary = salary;
        this.xxx = xxx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
*/
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
                ", birth=" + dateOfBirth +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", xxx=" + xxx +
                '}';
    }
}
