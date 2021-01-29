package com.andrzej.payroll.persist.repository;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {

    boolean existsWeekByAppUserAndWeekNumber(AppUser appUser, int weekNumber);

    Optional<Week> findByAppUserAndWeekNumber(AppUser appUser, int weekNumber);

    List<Week> findAllByAppUser(AppUser appUser);
}
