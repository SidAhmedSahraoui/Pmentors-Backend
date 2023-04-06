package com.example.authusersmicroservice.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
public class ApiResponse {

    private HttpStatusCode status;
    private String message;

}