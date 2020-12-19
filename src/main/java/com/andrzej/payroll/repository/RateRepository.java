package com.andrzej.payroll.repository;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Rate findByAppUser(AppUser appUser);

    boolean existsByAppUser(AppUser appUser);
}
