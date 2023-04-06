package com.example.authusersmicroservice.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {

    private HttpStatusCode status;
    private String message;
    private List<String> errors;


    public ApiError(HttpStatusCode status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
