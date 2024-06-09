package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.exception.NotCreatedException;
import com.example.rqchallenge.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class EmployController implements IEmployeeController {

    private final Logger logger = LoggerFactory.getLogger(EmployController.class);

    private final EmployeeService employeeService;
    private final int highestEarningEmployeeCount;
    private final ObjectMapper mapper;

    @Autowired
    public EmployController(EmployeeService employeeService,
                            @Value("${highestEarningEmployeeCount:10}") int highestEarningEmployeeCount,
                            ObjectMapper mapper) {

        this.employeeService = employeeService;
        this.highestEarningEmployeeCount = highestEarningEmployeeCount;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        try {
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } catch (URISyntaxException | InterruptedException e) {
            logger.error("Error while getting employee information");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        try {
            return ResponseEntity.ok(employeeService.searchEmployees(searchString));
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error("Error while searching employee information for search string {}", searchString);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        try {
            return employeeService.getEmployee(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException("Employee with id="+id+" is not found"));
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error("Error while fetching employee information for id {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            return employeeService.getHighestSalary()
                    .map(BigDecimal::intValue)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException("Employees not not found"));
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error("Error while fetching highest salary");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> topHighestEarningEmployeeNames = employeeService.getTopHighestEarningEmployeeNames(highestEarningEmployeeCount);
            return ResponseEntity.ok(topHighestEarningEmployeeNames);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error("Error while fetching top highest earning employee names");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        try {
            CreateEmployeeRequest request = mapper.convertValue(employeeInput, CreateEmployeeRequest.class);
            request.validate();
            return employeeService.create(request)
                    .map(employee -> ResponseEntity.status(CREATED).body(employee))
                    .orElseThrow(() -> new NotCreatedException("Employee not created"));
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error("Error while creating employee");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try {
            return employeeService.delete(id)
                    .map(Employee::getName)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException("Employees not not found"));
        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error("Error while creating employee");
            throw new RuntimeException(e);
        }
    }

}
