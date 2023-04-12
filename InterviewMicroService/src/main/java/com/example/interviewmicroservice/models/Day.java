package com.example.interviewmicroservice.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Day implements Serializable  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dayId ;
    @Enumerated(EnumType.STRING)
    private DayName dayName ;

    public Day (DayName dayName) {this.dayName = dayName;}
    public String getDayName() {
        return dayName.toString();
    }
}