package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Table(name = "book")
@Entity
@Getter
public class Book {

    @Id // 주요식별자(primary key)
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String title;

    private String author;
    private String code;
    private boolean isAvailable;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
