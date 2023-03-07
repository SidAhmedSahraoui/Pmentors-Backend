package com.example.authusersmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegisterRequest {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String password;
}