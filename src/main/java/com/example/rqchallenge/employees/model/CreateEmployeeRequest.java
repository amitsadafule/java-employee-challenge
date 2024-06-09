package com.example.rqchallenge.employees.model;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class CreateEmployeeRequest {

    @NotBlank(message = "Name cannot be null or empty")
    private String name;
    @NotNull(message = "Salary cannot be null or empty")
    private BigDecimal salary;
    @NotNull(message = "Age cannot be null or empty")
    private Integer age;

    public CreateEmployeeRequest() {
    }

    public CreateEmployeeRequest(String name, BigDecimal salary, Integer age) {
        this.name = name;
        this.salary = salary;
        this.age = age;
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

    public void validate() {
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(this);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateEmployeeRequest request = (CreateEmployeeRequest) o;
        return Objects.equals(name, request.name) && Objects.equals(salary, request.salary) && Objects.equals(age, request.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary, age);
    }

    @Override
    public String toString() {
        return "CreateEmployeeRequest{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}
