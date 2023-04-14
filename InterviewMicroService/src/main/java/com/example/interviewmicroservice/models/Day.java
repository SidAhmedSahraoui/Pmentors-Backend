package com.example.interviewmicroservice.models;
import com.example.interviewmicroservice.enums.DayName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "days")
public class Day implements Serializable  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dayId ;
    @Enumerated(EnumType.STRING)
    private DayName dayName ;

    @ManyToMany(mappedBy = "days")
    private Set<Provider> providers;

}