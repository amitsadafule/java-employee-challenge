package com.example.rqchallenge.employees.external;

import com.example.rqchallenge.exception.InvalidApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Component
public class ExternalEmployeeDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ExternalEmployeeDatabase.class);

    private final String endpoint;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public ExternalEmployeeDatabase(@Value("${employee.endpoint}") String endpoint,
                                    HttpClient httpClient,
                                    ObjectMapper mapper) {
        this.endpoint = endpoint;
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    public List<ExternalEmployee> getEmployees() throws URISyntaxException, IOException, InterruptedException {
        String GET_ALL_EMPLOYEES_CONTEXT_URL = "api/v1/employees";
        HttpResponse<String> employeesApiResponse = get(GET_ALL_EMPLOYEES_CONTEXT_URL);
        return getResult(employeesApiResponse, new TypeReference<>() {});
    }

    public Optional<ExternalEmployee> getEmployee(String id) throws URISyntaxException, IOException, InterruptedException {
        String GET_EMPLOYEE_CONTEXT_URL = "api/v1/employee/%s";
        HttpResponse<String> employeesApiResponse = get(String.format(GET_EMPLOYEE_CONTEXT_URL, id));
        ExternalEmployee result = getResult(employeesApiResponse, new TypeReference<>() {});
        return Optional.ofNullable(result);
    }

    public Optional<ExternalEmployee> create(ExternalEmployee externalEmployeeRequest) throws URISyntaxException, IOException, InterruptedException {
        String CREATE_EMPLOYEE_CONTEXT_URL = "api/v1/create";
        HttpResponse<String> employeesApiResponse = post(CREATE_EMPLOYEE_CONTEXT_URL, externalEmployeeRequest);
        ExternalEmployee result = getResult(employeesApiResponse, new TypeReference<>() {});
        return Optional.ofNullable(result);
    }

    public String deleteById(String id) throws URISyntaxException, IOException, InterruptedException {
        String DELETE_EMPLOYEE_CONTEXT_URL = "api/v1/delete/%s";
        HttpResponse<String> employeesApiResponse = delete(String.format(DELETE_EMPLOYEE_CONTEXT_URL, id));
        return getResult(employeesApiResponse, new TypeReference<>() {});
    }

    private HttpResponse<String> get(String contextUrl) throws URISyntaxException, IOException, InterruptedException {
        String finalEndPoint = endpoint + "/" + contextUrl;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(new URI(finalEndPoint))
                .build();
        return httpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String contextUrl, ExternalEmployee request) throws URISyntaxException, IOException, InterruptedException {
        String finalEndPoint = endpoint + "/" + contextUrl;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                .uri(new URI(finalEndPoint))
                .build();
        return httpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> delete(String contextUrl) throws URISyntaxException, IOException, InterruptedException {
        String finalEndPoint = endpoint + "/" + contextUrl;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(new URI(finalEndPoint))
                .build();
        return httpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private <T> T getResult(HttpResponse<String> apiResponse, TypeReference<ApiResponse<T>> valueTypeRef) throws JsonProcessingException {
        if(HttpStatus.valueOf(apiResponse.statusCode()).is2xxSuccessful()) {
            ApiResponse<T> response = mapper
                    .readValue(apiResponse.body(), valueTypeRef);
            return response.getData();
        }
        logger.error("Api did not return success response. Status code returned {}", apiResponse.statusCode());
        throw new InvalidApiResponse("Api did not return success response");
    }
}
