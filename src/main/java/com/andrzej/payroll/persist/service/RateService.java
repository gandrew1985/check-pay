package com.andrzej.payroll.persist.service;

import com.andrzej.payroll.core.exception.NotFoundException;
import com.andrzej.payroll.core.security.AppUserDetailsService;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Rate;
import com.andrzej.payroll.persist.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;
    private final AppUserDetailsService userDetailsService;

    public boolean existRateForUser(AppUser appUser) {
        return rateRepository.existsByAppUser(appUser);
    }

    public Rate findRateByUser(AppUser appUser) {
        appUser = userDetailsService.getLoggedUser();
        return rateRepository.findByAppUser(appUser);
    }

    public List<Rate> findAllRatesByAppUser(AppUser appUser) {
        appUser = userDetailsService.getLoggedUser();
        return rateRepository.findAllByAppUser(appUser);
    }

    public void createRate(Rate rate) {
        rateRepository.save(rate);
    }

    public Rate updateRate(Rate rate) {
        Rate rateEdited = rateRepository.findById(rate.getId())
                .orElseThrow(() -> new NotFoundException("Rate not found"));
        rateEdited.setHourlyRate(rate.getHourlyRate());
        rateEdited.setOvertimeRate(rate.getOvertimeRate());
        rateEdited.setNightRate(rate.getNightRate());
        rateEdited.setLocationRate(rate.getLocationRate());
        rateEdited.setAllowance(rate.getAllowance());
        rateEdited.setPensionScheme(rate.getPensionScheme());
        return rateEdited;
    }

    public Rate findRateById(Long id) {
        return rateRepository.findById(id).orElseThrow(() -> new NotFoundException("rate not found"));
    }

    public void deleteRateById(Long id) {
        rateRepository.deleteById(id);
    }
}