package com.andrzej.payroll.service;

import com.andrzej.payroll.configuration.WebSecurityConfig;
import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.exception.NotFoundException;
import com.andrzej.payroll.repository.AppUserRepository;
import com.andrzej.payroll.repository.RateRepository;
import com.andrzej.payroll.repository.WorkdayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UserDbService {

/*
* REVIEW
* Trwialny konstruktor w tej klasie może być zastąpiony Lombokiem którego i tak już używasz.
* jak dodasz nad nazwą klasy adnotacje @RequiredArgsConstructor, poniższe pola oznaczasz final (Jak używasz Intelij to powininen Ci to podkreślać)
* to wtedy możesz usunąć konstruktor. Dla czytelności zrobiłbym tak w każdej klasie i mocno Ci to polecam bo sobie oszczędzasz czas.
*/

    private AppUserRepository appUserRepository;
    private WebSecurityConfig securityConfig;
    private WorkdayRepository workdayRepository;
    private TimeService timeService;
    private RateRepository rateRepository;

    public UserDbService(AppUserRepository appUserRepository, WebSecurityConfig securityConfig,
                         WorkdayRepository workdayRepository, TimeService timeService,
                         RateRepository rateRepository) {
        this.appUserRepository = appUserRepository;
        this.securityConfig = securityConfig;
        this.workdayRepository = workdayRepository;
        this.timeService = timeService;
        this.rateRepository = rateRepository;
    }

    public void addUser(AppUser appUser) {
        appUser.setPassword(securityConfig.passwordEncoder().encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }

    public Workday findWorkdayById(Long id) throws NotFoundException {
        return workdayRepository.findById(id).orElseThrow(() -> new NotFoundException("Workday not found"));
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

    public Rate findRateByUser(AppUser appUser) {
        appUser = timeService.getLoggedUser();
        return rateRepository.findByAppUser(appUser);
    }

    public void saveWorkday(Workday workday) {
        workdayRepository.save(workday);
    }

    public boolean existRateForUser(AppUser appUser) {
        return rateRepository.existsByAppUser(appUser);
    }
}
