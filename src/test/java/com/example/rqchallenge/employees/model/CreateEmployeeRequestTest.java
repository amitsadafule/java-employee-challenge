package com.example.rqchallenge.employees.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class CreateEmployeeRequestTest {

    @Test
    public void shouldGiveErrorIfNameIsNull() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(null, BigDecimal.TEN, 12);
        try {
            request.validate();
        } catch (ConstraintViolationException ex) {
            String message = ex
                    .getConstraintViolations()
                    .stream()
                    .findFirst()
                    .get()
                    .getMessage();
            assertEquals("Name cannot be null or empty", message);
        }

    }

    @Test
    public void shouldGiveErrorIfNameIsEmpty() {
        CreateEmployeeRequest request = new CreateEmployeeRequest("", BigDecimal.TEN, 12);
        try {
            request.validate();
        } catch (ConstraintViolationException ex) {
            String message = ex
                    .getConstraintViolations()
                    .stream()
                    .findFirst()
                    .get()
                    .getMessage();
            assertEquals("Name cannot be null or empty", message);
        }
    }

    @Test
    public void shouldGiveErrorIfSalaryIsNull() {
        CreateEmployeeRequest request = new CreateEmployeeRequest("abc", null, 12);
        try {
            request.validate();
        } catch (ConstraintViolationException ex) {
            String message = ex
                    .getConstraintViolations()
                    .stream()
                    .findFirst()
                    .get()
                    .getMessage();
            assertEquals("Salary cannot be null or empty", message);
        }
    }

    @Test
    public void shouldGiveErrorIfAgeIsNull() {
        CreateEmployeeRequest request = new CreateEmployeeRequest("abc", BigDecimal.TEN, null);
        try {
            request.validate();
        } catch (ConstraintViolationException ex) {
            String message = ex
                    .getConstraintViolations()
                    .stream()
                    .findFirst()
                    .get()
                    .getMessage();
            assertEquals("Age cannot be null or empty", message);
        }
    }
}