package com.example.rqchallenge.employees.external;

import com.example.rqchallenge.employees.config.ApplicationConfiguration;
import com.example.rqchallenge.exception.InvalidApiResponse;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8080)
public class ExternalEmployeeDatabaseTest {

    private final String employeeEndpoint = "http://localhost:8080";
    private final HttpClient client = HttpClient.newBuilder().build();
    private final ExternalEmployeeDatabase externalEmployeeDatabase = new ExternalEmployeeDatabase(employeeEndpoint,
            client,
            ApplicationConfiguration.OBJECT_MAPPER);

    @Test
    public void shouldGetAllEmployees() throws URISyntaxException, IOException, InterruptedException {
        int id = 1;
        String tigerNixon = "Tiger Nixon";
        BigDecimal salary = new BigDecimal("320800");
        int age = 61;
        stubFor(get("/api/v1/employees")
                .willReturn(okJson(
                        "{" +
                                "\"status\": \"success\", " +
                                "\"data\": [ " +
                                "{" +
                                "\"id\": \"" + id + "\", " +
                                "\"employee_name\": \"" + tigerNixon + "\"," +
                                "\"employee_salary\": \"" + salary + "\"," +
                                "\"employee_age\": \"" + age + "\"," +
                                "\"profile_image\": \"\"" +
                                "}" +
                                "]" +
                                "}"
                ))
        );

        List<ExternalEmployee> actualEmployees = externalEmployeeDatabase.getEmployees();

        List<ExternalEmployee> expectedEmployees = List.of(
                new ExternalEmployee(id,
                        tigerNixon,
                        salary,
                        age));

        assertEquals(expectedEmployees, actualEmployees);

    }

    @Test
    public void shouldEmployeeDetailsById() throws URISyntaxException, IOException, InterruptedException {
        int id = 1;
        String tigerNixon = "Tiger Nixon";
        BigDecimal salary = new BigDecimal("320800");
        int age = 61;
        stubFor(get("/api/v1/employee/"+id)
                .willReturn(okJson(
                        "{" +
                                "\"status\": \"success\", " +
                                "\"data\": {" +
                                "\"id\": \"" + id + "\", " +
                                "\"employee_name\": \"" + tigerNixon + "\"," +
                                "\"employee_salary\": \"" + salary + "\"," +
                                "\"employee_age\": \"" + age + "\"," +
                                "\"profile_image\": \"\"" +
                                "}" +
                                "}"
                ))
        );

        ExternalEmployee actualEmployee = externalEmployeeDatabase.getEmployee(String.valueOf(id)).get();

        ExternalEmployee expectedEmployee = new ExternalEmployee(id,
                        tigerNixon,
                        salary,
                        age);

        assertEquals(expectedEmployee, actualEmployee);

    }

    @Test
    public void shouldCreateEmployeeDetails() throws URISyntaxException, IOException, InterruptedException {
        int id = 1;
        String tigerNixon = "Tiger Nixon";
        BigDecimal salary = new BigDecimal("320800");
        int age = 61;
        stubFor(post("/api/v1/create")
                .willReturn(okJson(
                        "{" +
                                "\"status\": \"success\", " +
                                "\"data\": {" +
                                "\"id\": \"" + id + "\", " +
                                "\"name\": \"" + tigerNixon + "\"," +
                                "\"salary\": \"" + salary + "\"," +
                                "\"age\": \"" + age + "\"" +
                                "}" +
                                "}"
                ))
        );

        ExternalEmployee externalEmployeeRequest = new ExternalEmployee(null, tigerNixon, salary, age);
        ExternalEmployee actualEmployee = externalEmployeeDatabase.create(externalEmployeeRequest).get();

        ExternalEmployee expectedEmployee = new ExternalEmployee(id,
                tigerNixon,
                salary,
                age);

        assertEquals(expectedEmployee, actualEmployee);

    }

    @Test
    public void shouldReturnEmptyCreateEmployeeIfNullData() throws URISyntaxException, IOException, InterruptedException {
        stubFor(post("/api/v1/create")
                .willReturn(okJson(
                        "{" +
                                "\"status\": \"success\", " +
                                "\"data\": null"+
                                "}"
                ))
        );

        ExternalEmployee externalEmployeeRequest = new ExternalEmployee(null, null, null, null);
        Optional<ExternalEmployee> actualEmployee = externalEmployeeDatabase.create(externalEmployeeRequest);

        assertTrue(actualEmployee.isEmpty());

    }

    @Test
    public void shouldDeleteEmployeeDetails() throws URISyntaxException, IOException, InterruptedException {
        int id = 1;
        stubFor(delete("/api/v1/delete/"+id)
                .willReturn(okJson("{\"status\":\"success\",\"data\":\""+id+"\",\"message\":\"Successfully! Record has been deleted\"}"
                ))
        );

        String stringId = String.valueOf(id);
        String deletedId = externalEmployeeDatabase.deleteById(stringId);
        assertEquals(stringId, deletedId);

    }

    @Test
    public void shouldGetInvalidApiResponseExceptionForIfApiReturnNonSuccessStatus() {
        stubFor(get("/api/v1/employees")
                .willReturn(status(429))
        );

        try {
            externalEmployeeDatabase.getEmployees();
        } catch (Exception e) {
            assertInstanceOf(InvalidApiResponse.class, e);
            assertEquals("Api did not return success response", e.getMessage());
        }

    }
}