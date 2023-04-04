package com.example.authusersmicroservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoRequest {

    private String id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;

}
