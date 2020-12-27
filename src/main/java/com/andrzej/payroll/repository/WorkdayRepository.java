package com.andrzej.payroll.repository;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Workday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    /*
     * REVIEW
     * 1. Takich metod jak findById(Long id) nie musisz pisać bo one juz są i same sie generują.
     */

    Workday findByDateAndAppUser(LocalDate date, AppUser appUser);

    boolean existsByDateAndAppUser(LocalDate date, AppUser appUser);

    List<Workday> findAllByAppUser(AppUser appUser);

    Optional<Workday> findById(Long id);

}
