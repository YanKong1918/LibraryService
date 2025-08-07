package com.library.repository;

import java.time.LocalDate;
import java.util.List;

import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

	// 해당 도서의 최신 대여 기록 조회
	Loan findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(int bookId);
	
	// 반납기한이 지난 대여 기록을 모두 조회
	@Query("SELECT l FROM Loan l JOIN FETCH l.user WHERE l.dueDate < :today AND l.returnDate IS NULL")
	List<Loan> findAllByDueDateBeforeAndReturnDateIsNull(@Param("today") LocalDate today);

	List<Loan> findAllByBookId(int bookId);

	List<Loan> findAllByUserId(int userId);

	// 연체된 도서가 있는지 확인
	boolean existsByUserAndReturnDateIsNullAndStatus(User user, Loan.Status status);

	// 사용자가 대출 중인 도서 수 확인
	int countByUserAndReturnDateIsNull(User user);

	@Query("""
			SELECT l
			FROM Loan l
			WHERE l.book.id = :book_id
				AND l.returnDate IS NULL
			ORDER BY l.loanDate DESC
			LIMIT 1
			""")
	Loan findLatestUnreturnedLoanByBookId(@Param("book_id") int bookId);
	// JPQL 에서는 LIMIT을 사용할 수 없기 때문에.. 이 QUERY는 유효하지 않다.
	// 참조. JPQL은 Entity 기준 쿼리이다.

	@Modifying
	@Query("""
			UPDATE Loan l
			SET l.returnDate = :return_date
			WHERE l.id = :id
			""")
	void updateReturnDateById(@Param("id") int id, @Param("return_date") LocalDate returnDate);

}
