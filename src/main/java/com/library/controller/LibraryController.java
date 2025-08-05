package com.library.controller;

import com.library.dto.*;
import com.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping("/books")
    public FindBooksResDto findBooks(@ModelAttribute FindBooksReqDto request) {
        return libraryService.findBooks(request);
    }

    @PostMapping("/books/borrow")
    public BorrowBooksResDto borrowBooks(@RequestBody BorrowBooksReqDto request) {
        return libraryService.borrowBooks(request);
    }

    @PutMapping("/books/return")
    public ReturnBooksResDto returnBooks(@RequestBody ReturnBooksReqDto request) {
        return libraryService.returnBooks(request);
    }

    @GetMapping("/books/loans")
    public BooksLoanRecordDto getBooksLoanRecord(@RequestParam( name = "book_id") int id) {
        return libraryService.getBooksLoanRecord(id);
    }

    @GetMapping("/users/loans")
    public UsersLoanRecordDto getUsersLoanRecord(@RequestParam(name = "user_id") int id) {
        return libraryService.getUsersLoanRecord(id);
    }

}
