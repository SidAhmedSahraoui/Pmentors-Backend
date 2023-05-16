package com.example.msgateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FallBackMethodController {

    @GetMapping("/authServiceFallBack")
    public String AuthServiceFallBack(){
        return "AuthServiceFallBack";
    }

    @GetMapping("/interviewServiceFallBack")
    public String InterviewServiceFallBack (){
        return "InterviewServiceFallBack";
    }
}
