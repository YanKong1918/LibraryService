package com.library.repository;

import com.library.dto.BookDto;

import java.util.List;

public interface CustomRepository {

    List<BookDto> findBooks(String keyword);

}
