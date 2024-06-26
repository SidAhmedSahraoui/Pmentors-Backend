package com.example.interviewmicroservice.services;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.DTOs.AppointmentRequest;
import com.example.interviewmicroservice.DTOs.GetPlanningResponse;
import com.example.interviewmicroservice.DTOs.ProxyUserResponse;
import com.example.interviewmicroservice.config.Message;

import com.example.interviewmicroservice.models.*;
import com.example.interviewmicroservice.proxies.AuthUsersProxy;
import com.example.interviewmicroservice.repositories.*;
import com.example.interviewmicroservice.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RequiredArgsConstructor
@Service
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final TimeSlotRepository timeSlotRepository;
    @Autowired
    private final DayRepository dayRepository;
    @Autowired
    private final AuthUsersProxy proxy;
    @Autowired
    private final ProviderRepository providerRepository;
    @Autowired
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public ResponseEntity<Object> getPlanning(String username){
        try {
            Provider provider = providerRepository.findByUsername(username).orElseGet(()-> null);;
            GetPlanningResponse response = new GetPlanningResponse(provider.getDays(),provider.getSlots());
            return new ResponseEntity<Object>( response, new HttpHeaders(), HttpStatus.OK);

        } catch (Exception e){
            log.info("error",e);
            return new ResponseEntity<Object>( new ApiResponse(HttpStatus.NOT_FOUND, "User not found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }


    }
    public ResponseEntity<Object> addPlanning(AddPlanningRequest request){

        try {

            ProxyUserResponse response = proxy.getUser(request.getEmail());

             Provider provider =
                     providerRepository.findByEmail(response.getEmail())
                     .orElseGet(() -> Provider.builder()
                     .providerId(null)
                     .username(response.getUsername())
                     .email(response.getEmail())
                     .slots(null)
                     .days(null)
                     .build());

                     Set<Day> days = new HashSet<>();
                     Set<TimeSlot> slots =  new HashSet<>();
                     request.getDaysAvailability().forEach(k -> {
                         if (dayRepository.existsById(k)){
                             Day day = dayRepository.findById(k).get();
                             days.add(day);
                         }
                     });
                     request.getSlotsAvailability().forEach(k -> {
                         if (timeSlotRepository.existsById(k)){
                             TimeSlot slot = timeSlotRepository.findById(k).get();
                             slots.add(slot);
                         }
                     });

                     provider.setDays(days);
                     provider.setSlots(slots);
                     providerRepository.save(provider);
                     GetPlanningResponse planningResponse = new GetPlanningResponse(days,slots);
                     return new ResponseEntity<Object>(
                             planningResponse,
                             new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e){
            log.info("error",e);
            return new ResponseEntity<Object>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "User not found"),
                    new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> addAppointment(AppointmentRequest request){
        try {
            ProxyUserResponse response = proxy.getUser(request.getClientEmail());
            Client client = clientRepository.findClientByEmail(response.getEmail())
                    .orElseGet(()-> Client.builder()
                            .clientId(null)
                            .username(response.getUsername())
                            .email(response.getEmail())
                            .appointments(null)
                            .build());
            Provider provider;
            try{
                provider = providerRepository.findByUsername(request.getProviderUsername()).get();
            } catch (Exception e){
                return new ResponseEntity<Object>(
                        new ApiResponse(HttpStatus.NOT_FOUND, "Provider not found"),
                        new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            if (provider.getDays().contains(
                    dayRepository.findById(request.getDay()).get()) &&
                provider.getSlots().contains(
                    timeSlotRepository.findById(request.getSlot()).get()
                )){

                TimeSlot newSlot = timeSlotRepository.findById(request.getSlot()).get();
                if (appointmentRepository.
                        existsByAppointmentDateAndClientEmailAndProviderEmailAndStartsAtAndEndsAt(
                                request.getDate(),
                                request.getClientEmail(),
                                provider.getEmail(),
                                newSlot.getStartsAt(),
                                newSlot.getEndsAt()
                                )){
                    return new ResponseEntity<Object>(
                            new ApiResponse(HttpStatus.BAD_REQUEST, "Time slot is already taken"),
                            new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else {
                    Client savedClient = clientRepository.save(client);
                    TimeSlot slot = timeSlotRepository.findById(request.getSlot()).get();

                    Appointment appointment = appointmentRepository.save(new Appointment(
                            null,
                            client.getEmail(),
                            provider.getEmail(),
                            request.getDate(),
                            slot.getStartsAt(),
                            slot.getEndsAt(),
                            false,
                            request.getLink(),
                            savedClient,
                            provider
                    ));
                    kafkaTemplate.send("topicNotification", new Message(
                            appointment.getClientEmail(),
                            appointment.getProviderEmail(),
                            "+213655649000",
                            "+213655649000",
                            slot.getStartsAt(),
                            request.getDate(),
                            LocalTime.now(),
                            LocalDate.now()
                    ));
                    return new ResponseEntity<Object>(
                            appointment,
                            new HttpHeaders(), HttpStatus.OK);
                }

            } else {
                return new ResponseEntity<Object>(
                        new ApiResponse(HttpStatus.BAD_REQUEST, "Provider is not available in this time and day"),
                        new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            return new ResponseEntity<Object>(
                    new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
                    new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> deleteAppointment(Integer appointmentId){
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).get();
            appointmentRepository.deleteByAppointmentId(appointmentId);
            kafkaTemplate.send("topicNotificationCancel", new Message(
                    appointment.getClientEmail(),
                    appointment.getProviderEmail(),
                    "+213655649000",
                    "+213655649000",
                    appointment.getStartsAt(),
                    appointment.getAppointmentDate(),
                    LocalTime.now(),
                    LocalDate.now()

            ));
            return new ResponseEntity<Object>(
                    new ApiResponse(HttpStatus.OK, "Appointment deleted"),
                    new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<Object>(
                    new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
                    new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
