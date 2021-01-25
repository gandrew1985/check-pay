package com.andrzej.payroll.persist.repository;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    Rate findByAppUser(AppUser appUser);

    List<Rate> findAllByAppUser(AppUser appUser);

    boolean existsByAppUser(AppUser appUser);
}
