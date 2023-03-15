package com.example.authusersmicroservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponse {

    private HttpStatusCode status;
    private String message;

}