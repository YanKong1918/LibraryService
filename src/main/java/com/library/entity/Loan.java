package com.library.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "loan")
@Entity
@Getter
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	private Loan() { }
	
	public Loan(User user, Book book, LocalDate loanDate, LocalDate dueDate) {
		this.user = user;
		this.book = book;
		this.loanDate = loanDate;
		this.dueDate = dueDate;
		this.returnDate = null;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	// 대여상태
	public enum Status{
		BORROWED,
		RETURNED,
		OVERDUE
	}
	
}
