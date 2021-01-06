package com.andrzej.payroll.repository;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    Rate findByAppUser(AppUser appUser);

    List<Rate> findAllByAppUser(AppUser appUser);

    boolean existsByAppUser(AppUser appUser);
}
