package com.library.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.library.entity.Loan;
import com.library.repository.LoanRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventScheduler {

	private final LoanRepository loanRepository;

	@Scheduled(cron = "0 0 0 * * *")
	public void checkOverDueLoans() {
		
		// 연체 상태 체킹
		List<Loan> list = loanRepository.findAllByDueDateBeforeAndReturnDateIsNullAndStatus(LocalDate.now(), Loan.Status.BORROWED);
		for (Loan loan : list) {
			loan.setStatus(Loan.Status.OVERDUE);
		}
	}
}
