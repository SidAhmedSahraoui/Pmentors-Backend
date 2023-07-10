package com.example.authusersmicroservice.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "providers")
public class Provider {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer providerId;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;
}
