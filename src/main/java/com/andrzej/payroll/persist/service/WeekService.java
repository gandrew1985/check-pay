package com.andrzej.payroll.persist.service;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Week;
import com.andrzej.payroll.persist.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WeekService {

    private final WeekRepository weekRepository;

    public void saveWeek(Week week) {
        weekRepository.save(week);
    }

    public List<Week> findAllWeeks() {
        return weekRepository.findAll();
    }

    public boolean existWeekForUserWithNumber(AppUser appUser, int weekNumber) {
        return weekRepository.existsWeekByAppUserAndWeekNumber(appUser, weekNumber);
    }
}
