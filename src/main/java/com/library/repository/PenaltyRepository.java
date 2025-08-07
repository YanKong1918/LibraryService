package com.library.repository;

import com.library.entity.Penalty;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {

    Optional<Penalty> findByUserAndEndDateIsNull(User user);

    // 유효한 페널티가 있는지 확인
    boolean existsByUserAndEndDateAfter(User user, LocalDate today);

}
