package com.library.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.Loan;
import com.library.entity.Penalty;
import com.library.entity.User;
import com.library.repository.LoanRepository;
import com.library.repository.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventScheduler {

    private final ObjectMapper objectMapper;
    private final LoanRepository loanRepository;
    private final PenaltyRepository penaltyRepository;

    /**
     * 도서 연체 상태 점검
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkOverDueLoans() {

        LocalDate today = LocalDate.now();
        // 연체 상태 체킹
        List<Loan> list = loanRepository.findAllByDueDateBeforeAndReturnDateIsNull(today);

        for (Loan loan : list) {
            loan.setStatus(Loan.Status.OVERDUE);

            User user = loan.getUser();
            Optional<Penalty> hasPenalty = penaltyRepository.findByUserAndEndDateIsNull(user);
            // 페널티가 없을 경우 생성
            if(hasPenalty.isEmpty()){
                Penalty newPenalty = new Penalty(user, today);
                penaltyRepository.save(newPenalty);
            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void timer() {
        log.info("*------------------ LOG TO NOTICE . . ");
    }

}
