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

    /* MAREK
    * To czy klasa jest długa może (ale nie musi oznaczać) klasa robi za wiele
    * Podstawowa zasada (patrz praktyki SOLID) jest taka że klasa nie powinna mieć wiecej niz jednego powodu do zmiany.
    * Zrozumienie co to własciwie oznacza przychodzi z doświadczeniem ale na przykładzie tej klasy mozna wywnioskować
    * że robisz tu operacje na Userze, WorkDay i Rate a to mogą być przyczyny do zmian. Jak bedziesz kiedyś coś zmieniał
    * w operacjach na WorkDay to bedziesz robił to w tej samej klasie gdzie znajdują sie operacje na Userze. Poniżej tego nie widać
    * ale jakby poniższe metody miały wspolne metody prywatne, to jakaś zmiana w działaniu na WorkDay mogłaby nieoczekiwanie
    * wpływać na operacje Userów i odwrotnie.
    * @Transactional nad klasą oznacza @Transactional nad każdą metodą publiczną, nie wiem czy to poprawnie ale ja zawsze umieszczam go nad klasą.
    * Tranzakcje nie są aż tak istnotne w SpringBoocie bo spring.jpa.open-in-view jest w nim właczony z defaultu a to oznacza
    * ze cześc rzeczy nie wymagają dedykowanej tranzakcji bo cały request jest jedną wielką tranzakcją. Jak ustawisz spring.jpa.open-in-view=false
    * w propertisach to np metoda addUser poniej rzuci Ci wyjatkiem bo nie bedzie mieć zadnej aktywnej tranzakcji
    * Obecnie tranzakcyjnośc sprowadza sie głównie do tego że jak coś sie wywali w czasie zapisywania to powinno to wszystkie
    * operacje na bazie powinny sie cofnąć. Oraz nie musisz robic save na każdym objeckie który updatujesz tylko dzieje sie to automatycznie.
    * Czyniąc historie krótką dawałbym @Transactional nad każdą klasą która jest servicem.
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
