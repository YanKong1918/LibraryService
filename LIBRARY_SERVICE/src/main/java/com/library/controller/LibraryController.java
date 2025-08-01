package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.library.common.domain.ApiResDto;
import com.library.dto.BookDto;
import com.library.dto.BorrowBooksReqDto;
import com.library.dto.FindBooksReqDto;
import com.library.dto.ReturnBooksReqDto;
import com.library.service.LibraryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LibraryController {
	
	private final LibraryService libraryService;
	
	@GetMapping("/books")
	public ApiResDto<List<BookDto>> findBooks(@ModelAttribute FindBooksReqDto request) {
		return libraryService.findBooks(request);
	}
	
	@PostMapping("/books/borrow")
	public ApiResDto<Void> borrowBooks(@RequestBody BorrowBooksReqDto request) {
		return libraryService.borrowBooks(request);
	}
	
	@PutMapping("/books/return")
	public ApiResDto<Void> returnBooks(@RequestBody ReturnBooksReqDto request) {
		return libraryService.returnBooks(request);
	}
}
