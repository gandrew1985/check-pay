package com.andrzej.payroll.persist.service;

import com.andrzej.payroll.core.exception.NotFoundException;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Week;
import com.andrzej.payroll.persist.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class WeekService {

    private final WeekRepository weekRepository;

    public void saveWeek(Week week) {
        weekRepository.save(week);
    }

    public List<Week> findAllWeeks() {
        return weekRepository.findAll();
    }

    public List<Week> findAllByAppUser(AppUser appUser) {
        return weekRepository.findAllByAppUser(appUser);
    }

    public boolean existWeekForUserWithNumber(AppUser appUser, int weekNumber) {
        return weekRepository.existsWeekByAppUserAndWeekNumber(appUser, weekNumber);
    }

    public Week findWeekByUserAndNumber(AppUser appUser, int weekNumber) {
        return weekRepository.findByAppUserAndWeekNumber(appUser, weekNumber)
                .orElseThrow(() -> new NotFoundException("Week does not exist"));
    }

    public Week updateWeek(Week week) {
        Week editedWeek = weekRepository
                .findByAppUserAndWeekNumber(week.getAppUser(), week.getWeekNumber())
                .orElseThrow(() -> new NotFoundException("Week does not exist"));

        editedWeek.setTotalBasicHours(week.getTotalBasicHours());
        editedWeek.setTotalNightHours(week.getTotalNightHours());
        editedWeek.setTotalBonusHours(week.getTotalBonusHours());
        editedWeek.setTotalLocationHours(week.getTotalBasicHours());
        editedWeek.setTotalMealAllowance(week.getTotalMealAllowance());
        editedWeek.setBasicHoursIncome(week.getBasicHoursIncome());
        editedWeek.setNightHoursIncome(week.getNightHoursIncome());
        editedWeek.setBonusHoursIncome(week.getBonusHoursIncome());
        editedWeek.setLocationHoursIncome(week.getLocationHoursIncome());
        editedWeek.setTotalIncome(week.getTotalIncome());
        editedWeek.setAfterTaxIncome(week.getAfterTaxIncome());
        return editedWeek;
    }

    public void deleteWeek(long weekId) {
        weekRepository.deleteById(weekId);
    }
}