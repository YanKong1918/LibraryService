package com.library.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

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
	
	
	public void setAvaiable(boolean isAvaiable) {
		this.isAvailable = isAvaiable;
	}
}
