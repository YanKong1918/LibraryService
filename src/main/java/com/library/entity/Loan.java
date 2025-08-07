package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Table(name = "loan")
@Entity
@Getter
public class Loan {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    @Column(nullable = false)
    private LocalDate loanDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int extensionCnt = 0;

    private Loan() { }

    public Loan(User user, Book book, LocalDate loanDate, LocalDate dueDate) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.status = Status.BORROWED;
        this.extensionCnt = 0;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setExtensionCnt(int extensionCnt) {
        this.extensionCnt = extensionCnt;
    }

    // 대여상태
    public enum Status {
        BORROWED,
        RETURNED,
        OVERDUE,
        LOST
    }

}
