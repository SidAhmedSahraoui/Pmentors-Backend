package com.example.interviewmicroservice.controllers;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.DTOs.AppointmentRequest;
import com.example.interviewmicroservice.models.*;
import com.example.interviewmicroservice.repositories.*;
import com.example.interviewmicroservice.responses.ApiResponse;
import com.example.interviewmicroservice.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/interviews")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DayRepository dayRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final AppointmentRepository appointmentRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPlanning(@PathVariable("id") String username){
        return appointmentService.getPlanning(username);
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

    @GetMapping("/appointments/client/{email}")
    public Collection<Appointment> getAppointmentsByClient(@PathVariable("email") String email){
        try {
            Client client = clientRepository.findClientByEmail(email).orElseGet(() -> null);
            Collection<Appointment> appointments = client.getAppointments();
            return appointments;
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/appointments/provider/{email}")
    public Collection<Appointment> getAppointmentsByProvider(@PathVariable("email") String email){
       try {
              Provider provider = providerRepository.findByEmail(email).orElseGet(() -> null);
              Collection<Appointment> appointments = provider.getAppointments();
              return appointments;
         } catch (Exception e){
              return null;
       }
    }
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable("id") Long id){
        return appointmentService.deleteAppointment(id);
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<Object> payment(@PathVariable("id") Long id){
        Appointment appointment = appointmentRepository.findById(id).orElseGet(() -> null);
        if (appointment != null){
            appointment.setIsPayed(true);
            appointmentRepository.save(appointment);
            return new ResponseEntity<Object>(
                    new ApiResponse(HttpStatus.OK, "Appointment payed"),
                    new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<Object>(
                new ApiResponse(HttpStatus.NOT_FOUND, "Appointment not found"),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
