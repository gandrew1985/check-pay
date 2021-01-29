package com.andrzej.payroll.persist.mapper;

import com.andrzej.payroll.persist.entity.Rate;
import com.andrzej.payroll.persist.entity.Week;
import com.andrzej.payroll.persist.entity.Workday;
import com.andrzej.payroll.web.dtos.RateDto;
import com.andrzej.payroll.web.dtos.WeekDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceMapper {

    public Workday mapToWorkday(WorkdayDto workdayDto) {
        return Workday.builder()
                .id(workdayDto.getId())
                .date(workdayDto.getDate())
                .weekNumber(workdayDto.getWeekNumber())
                .startTime(workdayDto.getStartTime())
                .finishTime(workdayDto.getFinishTime())
                .deductionTime(workdayDto.getDeductionTime())
                .totalPayableTime(workdayDto.getTotalPayableTime())
                .totalNightHours(workdayDto.getTotalNightHours())
                .totalBonusHours(workdayDto.getTotalBonusHours())
                .beforeTaxIncome(workdayDto.getBeforeTaxIncome())
                .afterTaxIncome(workdayDto.getAfterTaxIncome())
                .appUser(workdayDto.getAppUser())
                .build();
    }

    public WorkdayDto mapToWorkdayDto(Workday workday) {
        return WorkdayDto.builder()
                .id(workday.getId())
                .date(workday.getDate())
                .weekNumber(workday.getWeekNumber())
                .startTime(workday.getStartTime())
                .finishTime(workday.getFinishTime())
                .deductionTime(workday.getDeductionTime())
                .totalPayableTime(workday.getTotalPayableTime())
                .totalNightHours(workday.getTotalNightHours())
                .totalBonusHours(workday.getTotalBonusHours())
                .beforeTaxIncome(workday.getBeforeTaxIncome())
                .afterTaxIncome(workday.getAfterTaxIncome())
                .appUser(workday.getAppUser())
                .build();
    }

    public List<WorkdayDto> mapToWorkdayDtoList(List<Workday> workdayList) {
        return workdayList.stream()
                .map(workday -> WorkdayDto.builder()
                        .id(workday.getId())
                        .date(workday.getDate())
                        .weekNumber(workday.getWeekNumber())
                        .startTime(workday.getStartTime())
                        .finishTime(workday.getFinishTime())
                        .deductionTime(workday.getDeductionTime())
                        .totalPayableTime(workday.getTotalPayableTime())
                        .totalNightHours(workday.getTotalNightHours())
                        .totalBonusHours(workday.getTotalBonusHours())
                        .beforeTaxIncome(workday.getBeforeTaxIncome())
                        .afterTaxIncome(workday.getAfterTaxIncome())
                        .appUser(workday.getAppUser())
                        .build())
                .collect(Collectors.toList());
    }

    public Rate mapToRate(RateDto rateDto) {
        return Rate.builder()
                .id(rateDto.getId())
                .hourlyRate(rateDto.getHourlyRate())
                .locationRate(rateDto.getLocationRate())
                .nightRate(rateDto.getNightRate())
                .overtimeRate(rateDto.getOvertimeRate())
                .allowance(rateDto.getAllowance())
                .pensionScheme(rateDto.getPensionScheme())
                .appUser(rateDto.getAppUser())
                .build();
    }

    public RateDto mapToRateDto(Rate rate) {
        return RateDto.builder()
                .id(rate.getId())
                .hourlyRate(rate.getHourlyRate())
                .locationRate(rate.getLocationRate())
                .nightRate(rate.getNightRate())
                .overtimeRate(rate.getOvertimeRate())
                .allowance(rate.getAllowance())
                .pensionScheme(rate.getPensionScheme())
                .appUser(rate.getAppUser())
                .build();
    }

    public List<RateDto> mapToListRateDto(List<Rate> rateByUserSet) {
        return rateByUserSet.stream()
                .map(rate -> RateDto.builder()
                        .id(rate.getId())
                        .hourlyRate(rate.getHourlyRate())
                        .overtimeRate(rate.getOvertimeRate())
                        .nightRate(rate.getNightRate())
                        .locationRate(rate.getLocationRate())
                        .allowance(rate.getAllowance())
                        .pensionScheme(rate.getPensionScheme())
                        .appUser(rate.getAppUser())
                        .build())
                .collect(Collectors.toList());
    }

    public Week mapToWeek(WeekDto weekDto) {
        return Week.builder()
                .id(weekDto.getId())
                .weekNumber(weekDto.getWeekNumber())
                .totalBasicHours(weekDto.getTotalBasicHours())
                .totalNightHours(weekDto.getTotalNightHours())
                .totalLocationHours(weekDto.getTotalLocationHours())
                .totalBonusHours(weekDto.getTotalBonusHours())
                .basicHoursIncome(weekDto.getBasicHoursIncome())
                .nightHoursIncome(weekDto.getNightHoursIncome())
                .bonusHoursIncome(weekDto.getBonusHoursIncome())
                .locationHoursIncome(weekDto.getLocationHoursIncome())
                .totalIncome(weekDto.getTotalIncome())
                .afterTaxIncome(weekDto.getAfterTaxIncome())
                .appUser(weekDto.getAppUser())
                .build();
    }

    public List<WeekDto> mapToWeekDtoList(List<Week> weekList) {
        return (List<WeekDto>) weekList.stream()
                .map(week -> WeekDto.builder()
                        .id(week.getId())
                        .weekNumber(week.getWeekNumber())
                        .totalBasicHours(week.getTotalBasicHours())
                        .totalBonusHours(week.getTotalBonusHours())
                        .totalNightHours(week.getTotalNightHours())
                        .totalLocationHours(week.getTotalLocationHours())
                        .totalMealAllowance(week.getTotalMealAllowance())
                        .appUser(week.getAppUser())
                        .workdayDtoList(mapToWorkdayDtoList(week.getWorkdayList()))
                        .build())
                .collect(Collectors.toList());
    }

    public WeekDto mapToWeekDto(Week week) {
        return WeekDto.builder()
                .id(week.getId())
                .weekNumber(week.getWeekNumber())
                .totalBasicHours(week.getTotalBasicHours())
                .totalNightHours(week.getTotalNightHours())
                .totalLocationHours(week.getTotalLocationHours())
                .totalBonusHours(week.getTotalBonusHours())
                .totalMealAllowance(week.getTotalMealAllowance())
                .basicHoursIncome(week.getBasicHoursIncome())
                .bonusHoursIncome(week.getBonusHoursIncome())
                .nightHoursIncome(week.getNightHoursIncome())
                .locationHoursIncome(week.getLocationHoursIncome())
                .totalIncome(week.getTotalIncome())
                .afterTaxIncome(week.getAfterTaxIncome())
                .appUser(week.getAppUser())
                .workdayDtoList(mapToWorkdayDtoList(week.getWorkdayList()))
                .build();
    }
}
