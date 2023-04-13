package com.example.interviewmicroservice;

import com.example.interviewmicroservice.models.Day;
import com.example.interviewmicroservice.models.DayName;
import com.example.interviewmicroservice.models.TimeSlot;
import com.example.interviewmicroservice.repositories.DayRepository;
import com.example.interviewmicroservice.repositories.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalTime;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class InterviewMicroServiceApplication implements CommandLineRunner {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private DayRepository dayRepository;

    public static void main(String[] args) {

        SpringApplication.run(InterviewMicroServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Insert all slots
        for (int i = 0 ;  i <= 22 ; i++ ){
            LocalTime start = LocalTime.of(i, 0);
            LocalTime middle = LocalTime.of(i,30);
            LocalTime end = LocalTime.of(i + 1,0);
            timeSlotRepository.save(new TimeSlot(null, start,middle,null));
            timeSlotRepository.save(new TimeSlot(null, middle,end,null));
        }
        timeSlotRepository.save(new TimeSlot(null,LocalTime.of(23,0),LocalTime.of(23,30),null));
        timeSlotRepository.save(new TimeSlot(null,LocalTime.of(23,30),LocalTime.of(0,0),null));

        // Insert all days of week
        dayRepository.save(new Day(null, DayName.SUNDAY));
        dayRepository.save(new Day(null, DayName.MONDAY));
        dayRepository.save(new Day(null, DayName.TUESDAY));
        dayRepository.save(new Day(null, DayName.WEDNESDAY));
        dayRepository.save(new Day(null, DayName.THURSDAY));
        dayRepository.save(new Day(null, DayName.FRIDAY));
        dayRepository.save(new Day(null, DayName.SATURDAY));

    }
}
