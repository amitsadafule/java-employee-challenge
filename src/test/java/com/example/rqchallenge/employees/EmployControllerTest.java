package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void shouldGetAllEmployees() throws Exception {

        String employeeName = "Amit";
        int employeeId = 1;
        BigDecimal salary = BigDecimal.valueOf(12);
        int age = 15;
        when(employeeService.getAllEmployees())
                .thenReturn(List.of(new Employee(employeeId, employeeName, salary, age)));

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(employeeId)))
                .andExpect(jsonPath("$.[0].name", is(employeeName)))
                .andExpect(jsonPath("$.[0].salary", is(salary.intValue())))
                .andExpect(jsonPath("$.[0].age", is(age)));

    }


    @Test
    public void shouldAcceptSearchStringAsPathVariableToSearchEmployee() throws Exception {

        String amitName = "Amit";
        int amitId = 1;
        BigDecimal salary = BigDecimal.valueOf(12);
        int age = 15;
        String searchString = "ami";
        when(employeeService.searchEmployees(searchString))
                .thenReturn(List.of(new Employee(amitId, amitName, salary, age)));


        mockMvc.perform(get("/search/" + searchString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(amitId)))
                .andExpect(jsonPath("$.[0].name", is(amitName)))
                .andExpect(jsonPath("$.[0].salary", is(salary.intValue())))
                .andExpect(jsonPath("$.[0].age", is(age)));

    }

    @Test
    public void shouldAcceptIdAsPathVariableToGetSingleEmployee() throws Exception {

        String amitName = "Amit";
        int amitId = 1;
        BigDecimal salary = BigDecimal.valueOf(12);
        int age = 15;
        String idToSearch = String.valueOf(amitId);
        when(employeeService.getEmployee(idToSearch))
                .thenReturn(Optional.of(new Employee(amitId, amitName, salary, age)));


        mockMvc.perform(get("/" + idToSearch))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(amitId)))
                .andExpect(jsonPath("$.name", is(amitName)))
                .andExpect(jsonPath("$.salary", is(salary.intValue())))
                .andExpect(jsonPath("$.age", is(age)));

    }

    @Test
    public void shouldGiveNotFoundStatusCodeIfEmployeeIsNotFound() throws Exception {

        String idToSearch = "1";
        when(employeeService.getEmployee(idToSearch))
                .thenReturn(Optional.empty());


        mockMvc.perform(get("/" + idToSearch))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldGetHighestSalaryOutOfAllEmployees() throws Exception {
        when(employeeService.getHighestSalary())
                .thenReturn(Optional.of(BigDecimal.valueOf(4000)));


        mockMvc.perform(get("/highestSalary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(4000)));

    }

    @Test
    public void shouldGiveNotFoundStatusCodeIfEmployeeIsNotFoundForHighestSalary() throws Exception {

        when(employeeService.getHighestSalary())
                .thenReturn(Optional.empty());


        mockMvc.perform(get("/highestSalary"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldGetTopTenHighestEarningEmployeeNames() throws Exception {

        when(employeeService.getTopHighestEarningEmployeeNames(10))
                .thenReturn(List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"));

        mockMvc.perform(get("/topTenHighestEarningEmployeeNames"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", is("a")))
                .andExpect(jsonPath("$.[1]", is("b")))
                .andExpect(jsonPath("$.[2]", is("c")))
                .andExpect(jsonPath("$.[3]", is("d")))
                .andExpect(jsonPath("$.[4]", is("e")))
                .andExpect(jsonPath("$.[5]", is("f")))
                .andExpect(jsonPath("$.[6]", is("g")))
                .andExpect(jsonPath("$.[7]", is("h")))
                .andExpect(jsonPath("$.[8]", is("i")))
                .andExpect(jsonPath("$.[9]", is("j")));

    }

    @Test
    public void shouldCallCreateEmployeeApi() throws Exception {

        String name = "new name";
        BigDecimal salary = BigDecimal.valueOf(123);
        int age = 34;
        CreateEmployeeRequest employeeRequest = new CreateEmployeeRequest(name, salary, age);
        Employee createdEmployee = new Employee(1, name, salary, age);
        when(employeeService.create(employeeRequest))
                .thenReturn(Optional.of(createdEmployee));

        mockMvc.perform(post("/")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\": \""+name+"\",\"salary\": "+salary+",\"age\": "+age+"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.salary", is(salary.intValue())))
                .andExpect(jsonPath("$.age", is(age)));

    }

    @Test
    public void shouldGiveInternalServerErrorIfEmployeeNotCreated() throws Exception {

        String name = "new name";
        BigDecimal salary = BigDecimal.valueOf(123);
        int age = 34;
        CreateEmployeeRequest employeeRequest = new CreateEmployeeRequest(name, salary, age);
        when(employeeService.create(employeeRequest))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\": \""+name+"\",\"salary\": "+salary+",\"age\": "+age+"}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void shouldGiveBadRequestIfAnyRequestParameterIsMissing() throws Exception {

        BigDecimal salary = BigDecimal.valueOf(123);
        int age = 34;

        mockMvc.perform(post("/")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\": null,\"salary\": "+salary+",\"age\": "+age+"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldReturnSuccessIfSuccessfullyDeleted() throws Exception {

        int id = 1;
        String name = "new name";
        BigDecimal salary = BigDecimal.valueOf(123);
        int age = 34;
        Employee employee = new Employee(id, name, salary, age);
        when(employeeService.delete(String.valueOf(id)))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(delete("/"+id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(name)));

    }

    @Test
    public void shouldReturnNotFoundIfUserNotFoundInDeletionRequest() throws Exception {

        int id = 1;
        when(employeeService.delete(String.valueOf(id)))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/"+id))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
}