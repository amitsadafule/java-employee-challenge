package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.external.ExternalEmployee;
import com.example.rqchallenge.employees.external.ExternalEmployeeDatabase;
import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final ExternalEmployeeDatabase externalEmployeeDatabase;

    @Autowired
    public EmployeeService(ExternalEmployeeDatabase externalEmployeeDatabase) {

        this.externalEmployeeDatabase = externalEmployeeDatabase;
    }

    public List<Employee> getAllEmployees() throws IOException, URISyntaxException, InterruptedException {
        return externalEmployeeDatabase.getEmployees()
                .stream()
                .map(ExternalEmployee::toEmployee)
                .collect(Collectors.toList());
    }

    public List<Employee> searchEmployees(String searchString) throws IOException, URISyntaxException, InterruptedException {
        return externalEmployeeDatabase.getEmployees()
                .stream()
                .filter(externalEmployee -> externalEmployee.getName().contains(searchString))
                .map(ExternalEmployee::toEmployee)
                .collect(Collectors.toList());
    }

    public Optional<Employee> getEmployee(String id) throws IOException, URISyntaxException, InterruptedException {
        return externalEmployeeDatabase.getEmployee(id)
                .map(ExternalEmployee::toEmployee);
    }

    public Optional<BigDecimal> getHighestSalary() throws IOException, URISyntaxException, InterruptedException {
        return externalEmployeeDatabase.getEmployees()
                .stream()
                .map(ExternalEmployee::getSalary)
                .max(BigDecimal::compareTo);
    }

    public List<String> getTopHighestEarningEmployeeNames(int limit) throws IOException, URISyntaxException, InterruptedException {
        return externalEmployeeDatabase.getEmployees()
                .stream()
                .sorted((a, b) -> b.getSalary().compareTo(a.getSalary()))
                .limit(limit)
                .map(ExternalEmployee::getName)
                .collect(Collectors.toList());
    }

    public Optional<Employee> create(CreateEmployeeRequest employeeRequest) throws IOException, URISyntaxException, InterruptedException {
        return externalEmployeeDatabase.create(ExternalEmployee.from(employeeRequest))
                .map(ExternalEmployee::toEmployee);
    }

    public Optional<Employee> delete(String id) throws IOException, URISyntaxException, InterruptedException {
        Optional<ExternalEmployee> employee = externalEmployeeDatabase
                .getEmployee(id);
        if(employee.isEmpty()) return Optional.empty();
        externalEmployeeDatabase.deleteById(id);
        return employee.map(ExternalEmployee::toEmployee);
    }
}
