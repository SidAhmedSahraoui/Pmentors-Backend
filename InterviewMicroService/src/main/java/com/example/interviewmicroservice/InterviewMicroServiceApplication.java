package com.example.interviewmicroservice;

import com.example.interviewmicroservice.config.Message;
import com.example.interviewmicroservice.models.*;
import com.example.interviewmicroservice.enums.DayName;
import com.example.interviewmicroservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.time.LocalTime;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class InterviewMicroServiceApplication implements CommandLineRunner {

    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private ProviderRepository providerRepository;

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
        TimeSlot t1 = timeSlotRepository.save(new TimeSlot(null,LocalTime.of(23,0),LocalTime.of(23,30),null));
        TimeSlot t2 = timeSlotRepository.save(new TimeSlot(null,LocalTime.of(23,30),LocalTime.of(0,0),null));

        // Insert all days of week
        Day d1 = dayRepository.save(new Day(1, DayName.SUNDAY,null));
        Day d2 = dayRepository.save(new Day(2, DayName.MONDAY,null));
        dayRepository.save(new Day(3, DayName.TUESDAY,null));
        dayRepository.save(new Day(4, DayName.WEDNESDAY,null));
        dayRepository.save(new Day(5, DayName.THURSDAY,null));
        dayRepository.save(new Day(6, DayName.FRIDAY,null));
        dayRepository.save(new Day(7, DayName.SATURDAY,null));

        Provider provider = providerRepository.save(
                new Provider(null,"admin@admin.com","admin",null,null,null));

    }
}
