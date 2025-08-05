package com.library.repository;

import java.util.List;

import com.library.dto.BookDto;


public interface CustomRepository {
	
	List<BookDto> findBooks(String keyword);

}
