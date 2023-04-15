package com.example.interviewmicroservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "slots")
public class TimeSlot implements Serializable  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeSlotId ;

    @Column(nullable = false)
    private LocalTime startsAt;

    @Column(nullable = false)
    private LocalTime endsAt;

    @ManyToMany(mappedBy = "slots")
    @JsonIgnore
    private Set<Provider> providers;

}
