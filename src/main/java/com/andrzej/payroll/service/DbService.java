package com.andrzej.payroll.service;

import com.andrzej.payroll.configuration.WebSecurityConfig;
import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.exception.NotFoundException;
import com.andrzej.payroll.repository.AppUserRepository;
import com.andrzej.payroll.repository.RateRepository;
import com.andrzej.payroll.repository.WorkdayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DbService {

    /*czy ta klasa nie jest za dluga?
     * czy @Transactional nalezy stosowac nad metoda ktora posiada wiecej niz jedna transakcje,
     * czy lepiej nad klasa?
     */

    private final AppUserRepository appUserRepository;
    private final WebSecurityConfig securityConfig;
    private final WorkdayRepository workdayRepository;
    private final TimeService timeService;
    private final RateRepository rateRepository;

    public void addUser(AppUser appUser) {
        appUser.setPassword(securityConfig.passwordEncoder().encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }

    @Transactional
    public AppUser editUser(AppUser appUser) {
        AppUser userEdited = appUserRepository.findById(appUser.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        userEdited.setUsername(appUser.getUsername());
        userEdited.setPassword(securityConfig.passwordEncoder().encode(appUser.getPassword()));
        userEdited.setRole(appUser.getRole());
        return userEdited;
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
    }

    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    public Workday findWorkdayById(Long id) throws NotFoundException {
        return workdayRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workday not found"));
    }

    public List<Workday> getAllWorkdays() {
        return workdayRepository.findAll();
    }

    public List<Workday> findAllByAppUser(AppUser appUser) {
        return workdayRepository.findAllByAppUser(appUser);
    }

    public Workday getWorkdayByDateAndUser(LocalDate date, AppUser appUser) {
        return workdayRepository.findByDateAndAppUser(date, appUser);
    }

    public boolean existWorkdayByDateAndUser(LocalDate date, AppUser appUser) {
        return workdayRepository.existsByDateAndAppUser(date, appUser);
    }

    public void deleteWorkday(Long id) {
        workdayRepository.deleteById(id);
    }

    public List<Workday> findAllByUser(AppUser appUser) {
        return workdayRepository.findAllByAppUser(appUser);
    }

    public void saveWorkday(Workday workday) {
        workdayRepository.save(workday);
    }

    public boolean existRateForUser(AppUser appUser) {
        return rateRepository.existsByAppUser(appUser);
    }

    public Rate findRateByUser(AppUser appUser) {
        appUser = timeService.getLoggedUser();
        return rateRepository.findByAppUser(appUser);
    }

    public List<Rate> findAllRatesByAppUser(AppUser appUser) {
        appUser = timeService.getLoggedUser();
        return rateRepository.findAllByAppUser(appUser);
    }

    public Rate createRate(Rate rate) {
        return rateRepository.save(rate);
    }

    @Transactional
    public Rate updateRate(Rate rate) {
        Rate rateEdited = rateRepository.findById(rate.getId())
                .orElseThrow(() -> new NotFoundException("Rate not found"));
        rateEdited.setHourlyRate(rate.getHourlyRate());
        rateEdited.setOvertimeRate(rate.getOvertimeRate());
        rateEdited.setNightRate(rate.getNightRate());
        rateEdited.setLocationRate(rate.getLocationRate());
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
