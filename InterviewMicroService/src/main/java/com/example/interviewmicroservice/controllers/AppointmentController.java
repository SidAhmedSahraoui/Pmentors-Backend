package com.example.interviewmicroservice.controllers;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.DTOs.AppointmentRequest;
import com.example.interviewmicroservice.models.Day;
import com.example.interviewmicroservice.models.TimeSlot;
import com.example.interviewmicroservice.repositories.DayRepository;
import com.example.interviewmicroservice.repositories.TimeSlotRepository;
import com.example.interviewmicroservice.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/interviews")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DayRepository dayRepository;
    private final TimeSlotRepository timeSlotRepository;

    @GetMapping("/{email}")
    public ResponseEntity<Object> getPlanning(@PathVariable("email") String email){
        return appointmentService.getPlanning(email);
    }
    @PostMapping("/add-planning")
    public ResponseEntity<Object> addPlanning(
            @RequestBody AddPlanningRequest request){
        return appointmentService.addPlanning(request);
    }

    @PostMapping("/add-appointment")
    public ResponseEntity<Object> addAppointment(
            @RequestBody AppointmentRequest request){
        return appointmentService.addAppointment(request);
    }

    @GetMapping("/days")
    public List<Day> getDays(){
        return dayRepository.findAll();
    }

    @GetMapping("/slots")
    public List<TimeSlot> getSlots(){
        return timeSlotRepository.findAll();
    }
}
