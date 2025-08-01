package com.library.service;

import com.library.common.domain.ApiResDto;
import com.library.dto.BookDto;
import com.library.dto.BorrowBooksReqDto;
import com.library.dto.FindBooksReqDto;
import com.library.dto.ReturnBooksReqDto;

import java.util.List;

public interface LibraryService {

    ApiResDto<List<BookDto>> findBooks(FindBooksReqDto request);

    ApiResDto<Void> borrowBooks(BorrowBooksReqDto request);

    ApiResDto<Void> returnBooks(ReturnBooksReqDto request);

}
