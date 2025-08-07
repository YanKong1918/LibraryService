package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Table(name = "penalty")
@Entity
@Getter
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    private Penalty() { }

    public Penalty(User user, LocalDate today) {
        this.user = user;
        this.startDate = today;
        this.endDate = null;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
