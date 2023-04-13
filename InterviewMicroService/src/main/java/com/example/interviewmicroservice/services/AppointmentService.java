package com.example.interviewmicroservice.services;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.DTOs.ProxyUserResponse;
import com.example.interviewmicroservice.models.Provider;
import com.example.interviewmicroservice.proxies.AuthUsersProxy;
import com.example.interviewmicroservice.repositories.ProviderRepository;
import com.example.interviewmicroservice.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppointmentService {

    @Autowired
    private final AuthUsersProxy proxy;

    @Autowired
    private final ProviderRepository providerRepository;

    public ResponseEntity<Object> addPlanning(AddPlanningRequest request){
         try {
             ProxyUserResponse response = proxy.getProvider(request.getEmail(), request.getToken());

                    request.getDaysAvailability().forEach((k,v)->{

                    });
                     Provider provider = Provider.builder()
                             .providerId(response.getProviderId())
                             .username(response.getUsername())
                             .email(response.getEmail())
                             .providerSlots(null)
                             .build();
                     Provider savedProvider = providerRepository.save(provider);
                     return new ResponseEntity<Object>( savedProvider, new HttpHeaders(), HttpStatus.OK);

        } catch (Exception e){
            log.info("hello",e);
            return new ResponseEntity<Object>( new ApiResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service down"), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
