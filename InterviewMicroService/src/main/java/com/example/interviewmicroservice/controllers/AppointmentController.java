package com.example.interviewmicroservice.controllers;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/interviews")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/add-planning")
    public ResponseEntity<Object> getProvider(
            @RequestBody AddPlanningRequest request){
        return appointmentService.addPlanning(request);
    }
}
