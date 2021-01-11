package com.andrzej.payroll.mapper;

import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.RateDto;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.domain.WorkdayDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceMapper {

    public Workday mapToWorkday(WorkdayDto workdayDto) {
        return Workday.builder()
                .id(workdayDto.getId())
                .date(workdayDto.getDate())
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
                        .pensionScheme(rate.getPensionScheme())
                        .appUser(rate.getAppUser())
                        .build())
                .collect(Collectors.toList());
    }
}
