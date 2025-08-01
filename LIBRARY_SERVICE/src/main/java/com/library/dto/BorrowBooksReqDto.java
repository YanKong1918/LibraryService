package com.library.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.entity.Book;

import lombok.Data;

@Data
public class BorrowBooksReqDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("book_list")
	private List<Book> bookList;

}
