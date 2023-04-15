package com.example.interviewmicroservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private String providerEmail;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime startsAt;

    @Column(nullable = false)
    private LocalTime endsAt;

    @ManyToOne
    @JoinColumn(name="clientId")
    private Client client;

    @ManyToOne
    @JoinColumn(name="providerId")
    private Provider provider;

}
