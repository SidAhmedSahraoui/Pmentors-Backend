package com.example.interviewmicroservice.services;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.DTOs.ProxyUserResponse;
import com.example.interviewmicroservice.models.Day;
import com.example.interviewmicroservice.models.Provider;
import com.example.interviewmicroservice.models.TimeSlot;
import com.example.interviewmicroservice.proxies.AuthUsersProxy;
import com.example.interviewmicroservice.repositories.DayRepository;
import com.example.interviewmicroservice.repositories.ProviderRepository;
import com.example.interviewmicroservice.repositories.TimeSlotRepository;
import com.example.interviewmicroservice.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppointmentService {
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    private final DayRepository dayRepository;
    @Autowired
    private final AuthUsersProxy proxy;
    @Autowired
    private final ProviderRepository providerRepository;

    public ResponseEntity<Object> addPlanning(AddPlanningRequest request){

        try {

            ProxyUserResponse response = proxy.getProvider(request.getEmail(), request.getToken());

             Provider provider =
                     providerRepository.findByEmail(response.getEmail())
                     .orElseGet(() -> Provider.builder()
                     .providerId(response.getProviderId())
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
             return new ResponseEntity<Object>( provider, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e){
            log.info("hello",e);
            return new ResponseEntity<Object>( new ApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service down"), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
