package com.andrzej.payroll.persist.repository;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Workday;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    Optional<Workday> findByDateAndAppUser(LocalDate date, AppUser appUser);

    boolean existsByDateAndAppUser(LocalDate date, AppUser appUser);

    List<Workday> findAllByAppUser(AppUser appUser, Pageable pageable);

    List<Workday> findAllByWeekNumber(int weekNumber);
}
