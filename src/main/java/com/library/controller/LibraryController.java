package com.library.controller;

import com.library.dto.*;
import com.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    /**
     * 도서 검색
     */
    @GetMapping("/books")
    public FindBooksResDto findBooks(@ModelAttribute FindBooksReqDto request) {
        return libraryService.findBooks(request);
    }

    /**
     * 도서 대여
     */
    @PostMapping("/books/borrow")
    public BorrowBooksResDto borrowBooks(@RequestBody BorrowBooksReqDto request) {
        return libraryService.borrowBooks(request);
    }

    /**
     * 도서 반납
     */
    @PutMapping("/books/return")
    public ReturnBooksResDto returnBooks(@RequestBody ReturnBooksReqDto request) {
        return libraryService.returnBooks(request);
    }

    /**
     * 도서 대여 연장
     */
    @PostMapping("/books/borrow/extend")
    public ExtendLoanResDto extendLoan(@RequestBody ExtendLoanReqDto request) {
        return libraryService.extendLoan(request);
    }

    /**
     * 도서 별 대여 기록 조회
     */
    @GetMapping("/books/loans")
    public BooksLoanRecordDto getBooksLoanRecord(@RequestParam(name = "book_id") int id) {
        return libraryService.getBooksLoanRecord(id);
    }

    /**
     * 사용자 별 대여 기록 조회
     */
    @GetMapping("/users/loans")
    public UsersLoanRecordDto getUsersLoanRecord(@RequestParam(name = "user_id") int id) {
        return libraryService.getUsersLoanRecord(id);
    }

}
