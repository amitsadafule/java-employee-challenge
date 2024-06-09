package com.example.rqchallenge.employees.external;

import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalEmployee {

    private Integer id;
    @JsonProperty("employee_name")
    @JsonAlias("name")
    private String name;
    @JsonProperty("employee_salary")
    @JsonAlias("salary")
    private BigDecimal salary;
    @JsonProperty("employee_age")
    @JsonAlias("age")
    private Integer age;

    public ExternalEmployee() {
    }

    public ExternalEmployee(Integer id, String name, BigDecimal salary, Integer age) {

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

    public Employee toEmployee() {
        return new Employee(id, name, salary, age);
    }

    public static ExternalEmployee from(CreateEmployeeRequest request) {
        return new ExternalEmployee(null, request.getName(), request.getSalary(), request.getAge());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalEmployee that = (ExternalEmployee) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(salary, that.salary) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, age);
    }

    @Override
    public String toString() {
        return "ExternalEmployee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary='" + salary + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
