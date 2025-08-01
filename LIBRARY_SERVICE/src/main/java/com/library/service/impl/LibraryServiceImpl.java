package com.library.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.library.common.code.ERROR_CODE;
import com.library.common.domain.ApiResDto;
import com.library.common.exception.LoanNotFoundException;
import com.library.common.exception.UnavailableBookException;
import com.library.common.exception.UserNotFoundException;
import com.library.dto.BookDto;
import com.library.dto.BorrowBooksReqDto;
import com.library.dto.FindBooksReqDto;
import com.library.dto.ReturnBooksReqDto;
import com.library.entity.Book;
import com.library.entity.Loan;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.CustomRepository;
import com.library.repository.LoanRepository;
import com.library.repository.UserRepository;
import com.library.service.LibraryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final LoanRepository loanRepository;
	private final CustomRepository customRepository;

	@Override
	public ApiResDto<List<BookDto>> findBooks(FindBooksReqDto request) {
		try {

			String keyword = request.getKeyword();
			List<BookDto> bookList = customRepository.findBooks(keyword);
			if(bookList == null) {
				return ApiResDto.success();
			}
			return ApiResDto.success(bookList);

		} catch (Exception e) {
			log.error("## ERROR findBooks - {}", e.getMessage(), e);
			return ApiResDto.fail(ERROR_CODE.BAD_REQUEST);
		}
	}

	@Transactional
	@Override
	public ApiResDto<Void> borrowBooks(BorrowBooksReqDto request) {
		try {
			User user = userRepository.findById(request.getId()).orElseThrow(() -> new UserNotFoundException());

			LocalDate loanDate = LocalDate.now();
			LocalDate dueDate = loanDate.plusDays(7);

			for (Book B : request.getBookList()) {

				Book book = bookRepository.findById(B.getId()).orElseThrow();

				if (!book.isAvailable()) {
					throw new UnavailableBookException();
				}

				Loan loan = new Loan(user, book, loanDate, dueDate);

				loanRepository.save(loan);
				bookRepository.updateAvailabilityById(book.getId(), false);
			}

			return ApiResDto.success();

		} catch (Exception e) {
			log.error("## ERROR borrowBooks - {}", e.getMessage(), e);
			return ApiResDto.fail(ERROR_CODE.BAD_REQUEST);
		}
	}

	@Transactional
	@Override
	public ApiResDto<Void> returnBooks(ReturnBooksReqDto request) {
		try {
			for (Book B : request.getBookList()) {

				Book book = bookRepository.findById(B.getId()).orElseThrow();
				Loan loan = loanRepository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(book.getId());

				if (loan == null) {
					throw new LoanNotFoundException();
				}

				loan.setReturnDate(LocalDate.now());
				book.setAvaiable(true);
			}

			return ApiResDto.success();

		} catch (Exception e) {
			log.error("## ERROR returnBooks - {}", e.getMessage(), e);
			return ApiResDto.fail(ERROR_CODE.BAD_REQUEST);
		}
	}

}
