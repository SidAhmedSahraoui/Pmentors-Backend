package com.example.interviewmicroservice.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot implements Serializable  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeSlotId ;

    @Column(nullable = false)
    private LocalTime startsAt;

    @Column(nullable = false)
    private LocalTime endsAt;

}
