package com.library.service.impl;

import com.library.common.code.RES_CODE;
import com.library.common.exception.BookNotFoundException;
import com.library.common.exception.LoanNotFoundException;
import com.library.common.exception.UnavailableBookException;
import com.library.common.exception.UserNotFoundException;
import com.library.dto.*;
import com.library.entity.Book;
import com.library.entity.Loan;
import com.library.entity.Penalty;
import com.library.entity.User;
import com.library.repository.*;
import com.library.service.LibraryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final CustomRepository customRepository;
    private final PenaltyRepository penaltyRepository;


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

            // 1. 미납된 연체 도서 확인
            boolean hasOverdue = loanRepository.existsByUserAndReturnDateIsNullAndStatus(user, Loan.Status.OVERDUE);
            if (hasOverdue) {
                return BorrowBooksResDto.from(RES_CODE.DENIED);
            }

            // 2. 페널티 존재 여부 확인
            boolean activePenalty = penaltyRepository.existsByUserAndEndDateAfter(user, LocalDate.now());
            if (activePenalty) {
                return BorrowBooksResDto.from(RES_CODE.DENIED);
            }

            // 3. 현재 대출 중인 도서 수 확인
            int currentLoanCnt = loanRepository.countByUserAndReturnDateIsNull(user);

            List<Integer> bookList = request.getBookList();

            // 최대 대출 권수 초과 시 예외 처리
            if (currentLoanCnt + bookList.size() > 10) {
                return BorrowBooksResDto.exceed(RES_CODE.EXCEED_MAX_LOAN, currentLoanCnt);
            }

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
            User user = null;
            Loan lastLoan = null;

            for (int id : request.getBookList()) {
                Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
                Loan loan = loanRepository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(id);

                if (loan == null) {
                    throw new LoanNotFoundException();
                }

                // 연체 도서 기록
                if (loan.getStatus().equals(Loan.Status.OVERDUE)) {
                    lastLoan = loan;
                }

                user = loan.getUser();

                // 1. 도서 반납 처리
                loan.setReturnDate(LocalDate.now());
                loan.setStatus(Loan.Status.RETURNED);
                book.setAvailable(true);

            }

            // 2. 미반납 연체 도서 확인
            boolean hasUnreturned = loanRepository.existsByUserAndReturnDateIsNullAndStatus(user, Loan.Status.OVERDUE);
            if (!hasUnreturned) {
                Optional<Penalty> activePenalty = penaltyRepository.findByUserAndEndDateIsNull(user);
                Loan lastReturned = lastLoan;
                activePenalty.ifPresent(penalty -> {
                    if (lastReturned != null) {
                        long overdueDays = ChronoUnit.DAYS.between(lastReturned.getDueDate(), lastReturned.getReturnDate());
                        penalty.setEndDate(LocalDate.now().plusDays(overdueDays));
                    }
                });

            }

            return ReturnBooksResDto.from(RES_CODE.SUCCESS);

        } catch (Exception e) {
            log.error("## ERROR returnBooks - {}", e.getMessage(), e);
            return ReturnBooksResDto.from(RES_CODE.ERROR);
        }
    }

    /**
     * 도서 대여 연장
     */
    @Override
    public ExtendLoanResDto extendLoan(ExtendLoanReqDto request) {
        try {
            User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);
            LocalDate today = LocalDate.now();

            for (int id : request.getBookList()) {
                Loan loan = loanRepository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(id);

                if (loan == null) {
                    throw new LoanNotFoundException();
                }

                if (today.isAfter(loan.getLoanDate()) && loan.getStatus().equals(Loan.Status.BORROWED)
                        && loan.getExtensionCnt() < 1) {
                    loan.setDueDate(loan.getDueDate().plusDays(7));
                    loan.setExtensionCnt(1);
                }

            }

            return ExtendLoanResDto.from(RES_CODE.SUCCESS);

        } catch (Exception e) {
            log.error("## ERROR extendLoan - {}", e.getMessage(), e);
            return ExtendLoanResDto.from(RES_CODE.ERROR_404);
        }
    }

    @Override
    public BooksLoanRecordDto getBooksLoanRecord(int bookId) {
        try {
            Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
            List<Loan> list = loanRepository.findAllByBookId(bookId);

            if (list.isEmpty()) {
                return BooksLoanRecordDto.from(RES_CODE.SUCCESS_204);
            }

            return BooksLoanRecordDto.of(RES_CODE.SUCCESS, list);

        } catch (Exception e) {
            log.error("## ERROR getBooksLoanRecord - {}", e.getMessage(), e);
            return BooksLoanRecordDto.from(RES_CODE.ERROR_404);
        }
    }

    @Override
    public UsersLoanRecordDto getUsersLoanRecord(int userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            List<Loan> list = loanRepository.findAllByUserId(userId);

            if (list.isEmpty()) {
                return UsersLoanRecordDto.from(RES_CODE.SUCCESS_204);
            }

            return UsersLoanRecordDto.of(RES_CODE.SUCCESS, list);

        } catch (Exception e) {
            log.error("## ERROR getUsersLoanRecord - {}", e.getMessage(), e);
            return UsersLoanRecordDto.from(RES_CODE.ERROR_404);
        }
    }

}
