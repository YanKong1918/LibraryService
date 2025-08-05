package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String name;

    private LocalDate registerDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Loan> loans;

}
