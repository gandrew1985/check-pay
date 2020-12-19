package com.andrzej.payroll.mapper;

import com.andrzej.payroll.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceMapper {

    public Workday mapToWorkday(WorkdayDto workdayDto) {
        return Workday.builder()
                .date(workdayDto.getDate())
                .startTime(workdayDto.getStartTime())
                .finishTime(workdayDto.getFinishTime())
                .deductionTime(workdayDto.getDeductionTime())
                .build();
    }

    public WorkdayDto mapToWorkdayDto(Workday workday) {
        return WorkdayDto.builder()
                .date(workday.getDate())
                .startTime(workday.getStartTime())
                .finishTime(workday.getFinishTime())
                .deductionTime(workday.getDeductionTime())
                .build();
    }

    public List<WorkdayDto> mapToWorkdayDtoList(List<Workday> workdayList) {
        return workdayList.stream()
                .map(workday -> new WorkdayDto(workday.getId(),workday.getDate(),
                        workday.getStartTime(),
                        workday.getFinishTime(),workday.getDeductionTime()))
                .collect(Collectors.toList());
    }

    public Rate mapToRate(RateDto rateDto) {
        return Rate.builder()
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
                .hourlyRate(rate.getHourlyRate())
                .locationRate(rate.getLocationRate())
                .nightRate(rate.getNightRate())
                .overtimeRate(rate.getOvertimeRate())
                .pensionScheme(rate.getPensionScheme())
                .appUser(rate.getAppUser())
                .build();
    }
}
