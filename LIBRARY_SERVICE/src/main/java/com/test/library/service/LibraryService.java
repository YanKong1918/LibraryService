package com.test.library.service;

import com.test.library.dto.FindBooksReqDto;
import com.test.library.dto.FindBooksResDto;

public interface LibraryService {

	public FindBooksResDto findBooks(FindBooksReqDto request);

}
