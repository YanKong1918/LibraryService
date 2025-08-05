package com.library.service.impl;

import com.library.common.code.RES_CODE;
import com.library.common.exception.BookNotFoundException;
import com.library.common.exception.LoanNotFoundException;
import com.library.common.exception.UnavailableBookException;
import com.library.common.exception.UserNotFoundException;
import com.library.dto.*;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final CustomRepository customRepository;


    /**
     * 도서 검색
     */
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


    /**
     * 도서 대여
     */
    @Transactional
    @Override
    public BorrowBooksResDto borrowBooks(BorrowBooksReqDto request) {

        try {
            User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);
            List<Integer> bookList = request.getBookList();

            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusDays(7);

            for (int id : bookList) {

                Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

                if (!book.isAvailable()) {
                    throw new UnavailableBookException();
                }

                Loan loan = new Loan(user, book, loanDate, dueDate);

                loanRepository.save(loan);
                book.setAvailable(false);
            }

            return BorrowBooksResDto.from(RES_CODE.SUCCESS);

        } catch (Exception e) {
            log.error("## ERROR borrowBooks - {}", e.getMessage(), e);
            return BorrowBooksResDto.from(RES_CODE.ERROR);
        }
    }


    /**
     * 도서 반납
     */
    @Transactional
    @Override
    public ReturnBooksResDto returnBooks(ReturnBooksReqDto request) {
        try {
            for (int id : request.getBookList()) {

                Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
                Loan loan = loanRepository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(id);

                if (loan == null) {
                    throw new LoanNotFoundException();
                }

                loan.setReturnDate(LocalDate.now());
                loan.setStatus(Loan.Status.RETURNED);
                book.setAvailable(true);
            }

            return ReturnBooksResDto.from(RES_CODE.SUCCESS);

        } catch (Exception e) {
            log.error("## ERROR returnBooks - {}", e.getMessage(), e);
            return ReturnBooksResDto.from(RES_CODE.ERROR);
        }
    }

}
