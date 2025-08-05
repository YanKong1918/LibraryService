package com.library.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "users")
@Entity
@Getter
public class User {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	private LocalDate registerDate;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Loan> loans;
}
