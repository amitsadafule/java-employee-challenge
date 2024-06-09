package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.external.ExternalEmployee;
import com.example.rqchallenge.employees.external.ExternalEmployeeDatabase;
import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    private ExternalEmployeeDatabase externalEmployeeDatabase;

    @BeforeEach
    public void setUp() {
        externalEmployeeDatabase = mock(ExternalEmployeeDatabase.class);
        employeeService = new EmployeeService(externalEmployeeDatabase);
    }

    @AfterEach
    public void clean() {
        reset(externalEmployeeDatabase);
    }

    @Test
    public void shouldGetAllEmployeesFromExternalDatabase() throws URISyntaxException, IOException, InterruptedException {

        int id = 1;
        String name = "name";
        BigDecimal salary = BigDecimal.valueOf(300000);
        int age = 15;
        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of(new ExternalEmployee(id, name, salary, age)));

        List<Employee> allEmployees = employeeService.getAllEmployees();
        List<Employee> expectedEmployees = List.of(new Employee(id, name, salary, age));

        assertEquals(expectedEmployees, allEmployees);
    }

    @Test
    public void shouldGetEmployeesContainingGivenSearchString() throws URISyntaxException, IOException, InterruptedException {

        int amitId = 1;
        String amitName = "amitName";
        int sumitId = 2;
        String sumitName = "sumitName";
        int otherId = 3;
        String otherName = "other";
        BigDecimal salary = BigDecimal.valueOf(300000);
        int age = 15;
        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of(
                        new ExternalEmployee(amitId, amitName, salary, age),
                        new ExternalEmployee(sumitId, sumitName, salary, age),
                        new ExternalEmployee(otherId, otherName, salary, age)));

        String searchString = "mi";
        List<Employee> allEmployees = employeeService.searchEmployees(searchString);
        List<Employee> expectedEmployees = List.of(
                new Employee(amitId, amitName, salary, age),
                new Employee(sumitId, sumitName, salary, age));

        assertEquals(expectedEmployees, allEmployees);
    }

    @Test
    public void shouldGetEmployeesMatchingGivenSearchString() throws URISyntaxException, IOException, InterruptedException {

        int amitId = 1;
        String amitName = "amit";
        int sumitId = 2;
        String sumitName = "sumitName";
        int otherId = 3;
        String otherName = "other";
        BigDecimal salary = BigDecimal.valueOf(300000);
        int age = 15;
        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of(
                        new ExternalEmployee(amitId, amitName, salary, age),
                        new ExternalEmployee(sumitId, sumitName, salary, age),
                        new ExternalEmployee(otherId, otherName, salary, age)));

        String searchString = "amit";
        List<Employee> allEmployees = employeeService.searchEmployees(searchString);
        List<Employee> expectedEmployees = List.of(
                new Employee(amitId, amitName, salary, age));

        assertEquals(expectedEmployees, allEmployees);
    }

    @Test
    public void shouldReturnEmployeeByIdIfFoundInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        int amitId = 1;
        String amitName = "amit";
        BigDecimal salary = BigDecimal.valueOf(300000);
        int age = 15;
        String idToSearch = String.valueOf(amitId);
        when(externalEmployeeDatabase.getEmployee(idToSearch))
                .thenReturn(Optional.of(new ExternalEmployee(amitId, amitName, salary, age)));

        Optional<Employee> employee = employeeService.getEmployee(idToSearch);
        Employee expectedEmployee = new Employee(amitId, amitName, salary, age);

        assertEquals(expectedEmployee, employee.get());
    }

    @Test
    public void shouldReturnNoEmployeeByIdIfNotFoundInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        String idToSearch = "1";
        when(externalEmployeeDatabase.getEmployee(idToSearch))
                .thenReturn(Optional.empty());

        Optional<Employee> employee = employeeService.getEmployee(idToSearch);

        assertTrue(employee.isEmpty());
    }

    @Test
    public void shouldReturnHighestSalaryOfEmployeeIfFoundInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        int amitId = 1;
        String amitName = "amit";
        int sumitId = 2;
        String sumitName = "sumitName";
        int otherId = 3;
        String otherName = "other";
        BigDecimal amitSalary = BigDecimal.valueOf(100);
        BigDecimal sumitSalary = BigDecimal.valueOf(3000);
        BigDecimal otherSalary = BigDecimal.valueOf(200);
        int age = 15;
        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of(
                        new ExternalEmployee(amitId, amitName, amitSalary, age),
                        new ExternalEmployee(sumitId, sumitName, sumitSalary, age),
                        new ExternalEmployee(otherId, otherName, otherSalary, age)));

        Optional<BigDecimal> employee = employeeService.getHighestSalary();
        BigDecimal expectedHighestSalary = sumitSalary;

        assertEquals(expectedHighestSalary, employee.get());
    }

    @Test
    public void shouldReturnNoHighestSalaryIfNotFoundInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of());

        Optional<BigDecimal> employee = employeeService.getHighestSalary();

        assertTrue(employee.isEmpty());
    }

    @Test
    public void shouldReturnTopHighestEarningEmployeeNamesIfFoundInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        int amitId = 1;
        String amitName = "amit";
        int sumitId = 2;
        String sumitName = "sumitName";
        int otherId = 3;
        String riteshName = "other";
        BigDecimal amitSalary = BigDecimal.valueOf(100);
        BigDecimal sumitSalary = BigDecimal.valueOf(3000);
        BigDecimal riteshSalary = BigDecimal.valueOf(200);
        int age = 15;
        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of(
                        new ExternalEmployee(amitId, amitName, amitSalary, age),
                        new ExternalEmployee(sumitId, sumitName, sumitSalary, age),
                        new ExternalEmployee(otherId, riteshName, riteshSalary, age)));

        int limit = 2;
        List<String> actualTopHighestEarningEmployeeNames = employeeService.getTopHighestEarningEmployeeNames(limit);
        List<String> expectedTopHighestEarningEmployeeNames = List.of(sumitName, riteshName);

        assertEquals(expectedTopHighestEarningEmployeeNames, actualTopHighestEarningEmployeeNames);
    }

    @Test
    public void shouldNotReturnTopHighestEarningEmployeeNamesIfNotFoundInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        when(externalEmployeeDatabase.getEmployees())
                .thenReturn(List.of());

        int limit = 2;
        List<String> actualTopHighestEarningEmployeeNames = employeeService.getTopHighestEarningEmployeeNames(limit);

        assertTrue(actualTopHighestEarningEmployeeNames.isEmpty());
    }

    @Test
    public void shouldCreateEmployeeInRemoteDB() throws URISyntaxException, IOException, InterruptedException {

        String amitName = "amit";
        BigDecimal amitSalary = BigDecimal.valueOf(100);
        int age = 15;
        ExternalEmployee externalEmployeeRequest = new ExternalEmployee(null, amitName, amitSalary, age);
        int amitId = 1;
        ExternalEmployee createdEmployee = new ExternalEmployee(amitId, amitName, amitSalary, age);
        when(externalEmployeeDatabase.create(externalEmployeeRequest))
                .thenReturn(Optional.of(createdEmployee));


        CreateEmployeeRequest request = new CreateEmployeeRequest(amitName, amitSalary, age);
        Employee actualEmployee = employeeService.create(request).get();
        Employee expectedEmployee = new Employee(amitId, amitName, amitSalary, age);

        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void shouldGiveEmptyEmployeeIfInRemoteDBDoesNotReturnData() throws URISyntaxException, IOException, InterruptedException {

        String amitName = "amit";
        BigDecimal amitSalary = BigDecimal.valueOf(100);
        int age = 15;
        ExternalEmployee externalEmployeeRequest = new ExternalEmployee(null, amitName, amitSalary, age);
        when(externalEmployeeDatabase.create(externalEmployeeRequest))
                .thenReturn(Optional.empty());


        CreateEmployeeRequest request = new CreateEmployeeRequest(amitName, amitSalary, age);
        Optional<Employee> actualEmployee = employeeService.create(request);

        assertTrue(actualEmployee.isEmpty());
    }

    @Test
    public void shouldReturnDeletedEmployeeIfFoundInRemoteDBWhileEmployeeDeletion() throws URISyntaxException, IOException, InterruptedException {

        int amitId = 1;
        String amitName = "amit";
        BigDecimal amitSalary = BigDecimal.valueOf(100);
        int age = 15;
        String id = String.valueOf(amitId);
        when(externalEmployeeDatabase.getEmployee(id))
                .thenReturn(Optional.of(
                        new ExternalEmployee(amitId, amitName, amitSalary, age)));
        when(externalEmployeeDatabase.deleteById(id)).thenReturn(id);

        Employee deletedEmployee = employeeService.delete(id).get();
        Employee expectedDeletedEmployee = new Employee(amitId, amitName, amitSalary, age);

        assertEquals(expectedDeletedEmployee, deletedEmployee);
    }

    @Test
    public void shouldReturnEmptyEmployeeIfNotFoundInRemoteDBWhileDeletion() throws URISyntaxException, IOException, InterruptedException {

        String id = "1";
        when(externalEmployeeDatabase.getEmployee(id))
                .thenReturn(Optional.empty());

        Optional<Employee> employee = employeeService.delete(id);

        assertTrue(employee.isEmpty());
    }
}