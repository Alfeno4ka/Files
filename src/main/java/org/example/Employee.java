package org.example;

public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String country;
    private int age;

    public Employee() {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
