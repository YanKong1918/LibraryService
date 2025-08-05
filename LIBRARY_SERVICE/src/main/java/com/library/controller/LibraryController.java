package com.library.controller;

import com.library.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.library.service.LibraryService;

import lombok.RequiredArgsConstructor;

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

}
