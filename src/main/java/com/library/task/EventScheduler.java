package com.library.task;

import com.library.entity.Loan;
import com.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final LoanRepository loanRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkOverDueLoans() {

        LocalDate today = LocalDate.now();
        // 연체 상태 체킹
        List<Loan> list = loanRepository.findAllByDueDateBeforeAndReturnDateIsNull(today);

        for (Loan loan : list) {
            loan.setStatus(Loan.Status.OVERDUE);
        }
    }
}
