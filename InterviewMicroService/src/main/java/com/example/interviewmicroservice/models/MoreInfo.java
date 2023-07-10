package com.example.interviewmicroservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "more_info")
public class MoreInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer moreInfoId;

    private Integer appointmentId;

    private String domain;
    private String description;

    @Lob
    private byte[] file;
}
