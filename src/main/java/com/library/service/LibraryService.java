package com.library.service;

import com.library.dto.*;

public interface LibraryService {

    FindBooksResDto findBooks(FindBooksReqDto request);

    BorrowBooksResDto borrowBooks(BorrowBooksReqDto request);

    ReturnBooksResDto returnBooks(ReturnBooksReqDto request);

    ExtendLoanResDto extendLoan(ExtendLoanReqDto request);

    BooksLoanRecordDto getBooksLoanRecord(int id);

    UsersLoanRecordDto getUsersLoanRecord(int id);

}
