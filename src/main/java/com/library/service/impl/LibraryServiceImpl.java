package com.library.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.library.common.code.RES_CODE;
import com.library.common.exception.BookNotFoundException;
import com.library.dto.*;
import org.springframework.stereotype.Service;

import com.library.common.exception.LoanNotFoundException;
import com.library.common.exception.UnavailableBookException;
import com.library.common.exception.UserNotFoundException;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final LoanRepository loanRepository;
	private final CustomRepository customRepository;

	@Override
	public FindBooksResDto findBooks(FindBooksReqDto request) {
		try {

			String keyword = request.getKeyword();
			List<BookDto> bookList = customRepository.findBooks(keyword);
			if (bookList == null) {
				return FindBooksResDto.from(RES_CODE.SUCCESS_204);
			}
			return FindBooksResDto.of(RES_CODE.SUCCESS_200, bookList);

		} catch (Exception e) {
			log.error("## ERROR findBooks - {}", e.getMessage(), e);
			return FindBooksResDto.from(RES_CODE.ERROR_404);
		}
	}


	@Transactional
	@Override
	public BorrowBooksResDto borrowBooks(BorrowBooksReqDto request) {

		try{
			User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);

			LocalDate loanDate = LocalDate.now();
			LocalDate dueDate = loanDate.plusDays(7);

			for (Book B : request.getBookList()) {

				Book book = bookRepository.findById(B.getId()).orElseThrow(BookNotFoundException::new);

				if (!book.isAvailable()) {
					throw new UnavailableBookException();
				}

				Loan loan = new Loan(user, book, loanDate, dueDate);

				loanRepository.save(loan);
				bookRepository.updateAvailabilityById(book.getId(), false);
			}

			return BorrowBooksResDto.from(RES_CODE.SUCCESS);

		}catch (Exception e) {
			log.error("## ERROR borrowBooks - {}", e.getMessage(), e);
			return BorrowBooksResDto.from(RES_CODE.ERROR);
		}
	}

	@Transactional
	@Override
	public ReturnBooksResDto returnBooks(ReturnBooksReqDto request) {
		try {
			for (Book B : request.getBookList()) {

				Book book = bookRepository.findById(B.getId()).orElseThrow();
				Loan loan = loanRepository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(book.getId());

				if (loan == null) {
					throw new LoanNotFoundException();
				}

				loan.setReturnDate(LocalDate.now());
				book.setAvailable(true);
			}

			return ReturnBooksResDto.from(RES_CODE.SUCCESS);

		} catch (Exception e) {
			log.error("## ERROR returnBooks - {}", e.getMessage(), e);
			return ReturnBooksResDto.from(RES_CODE.ERROR);
		}
	}

}
