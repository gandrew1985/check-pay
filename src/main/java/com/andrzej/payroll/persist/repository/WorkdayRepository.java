package com.andrzej.payroll.persist.repository;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Workday;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    Workday findByDateAndAppUser(LocalDate date, AppUser appUser);

    boolean existsByDateAndAppUser(LocalDate date, AppUser appUser);

    List<Workday> findAllByAppUser(AppUser appUser, Pageable pageable);
}
