package com.example.rqchallenge.employees.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Employee {
    private Integer id;
    private String name;
    private BigDecimal salary;
    private Integer age;

    public Employee() {
    }

    public Employee(Integer id, String name, BigDecimal salary, Integer age) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(salary, employee.salary) && Objects.equals(age, employee.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, age);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}
