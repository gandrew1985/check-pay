package com.andrzej.payroll.core.logic;

import com.andrzej.payroll.persist.mapper.ServiceMapper;
import com.andrzej.payroll.persist.service.WorkdayService;
import com.andrzej.payroll.web.dtos.RateDto;
import com.andrzej.payroll.web.dtos.WeekDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekCalculator {

    private final ServiceMapper serviceMapper;
    private final WorkdayService workdayService;

    public WeekDto getWeekByNumber(int weekNumber, WeekDto weekDto, RateDto rateDto) {

        long totalBasicHours = 0L;
        long totalNightHours = 0L;
        long totalBonusHours = 0L;
        int daysNumber = 0;

        List<WorkdayDto> workdayDtoList = serviceMapper.mapToWorkdayDtoList(workdayService.getAllWorkdays());
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 7);
        Map<Integer, List<WorkdayDto>> datesGroupedByWeekNumber = workdayDtoList.stream()
                .collect(Collectors.groupingBy(workdayDto1 ->
                        workdayDto1.getDate().get(weekFields.weekOfYear()) + 1));

        for (Map.Entry<Integer, List<WorkdayDto>> entry : datesGroupedByWeekNumber.entrySet()) {
            List<WorkdayDto> daysByWeek = datesGroupedByWeekNumber.get(weekNumber);
            totalBasicHours = daysByWeek.stream()
                    .map(workdayDto -> workdayDto.getTotalPayableTime().toMinutes())
                    .reduce(0L, (sum, current) -> sum = sum + current);
            totalNightHours = daysByWeek.stream()
                    .map(workdayDto -> workdayDto.getTotalNightHours().toMinutes())
                    .reduce(0L, (sum, current) -> sum = sum + current);
            totalBonusHours = daysByWeek.stream()
                    .map(workdayDto -> workdayDto.getTotalBonusHours().toMinutes())
                    .reduce(0L, (sum, current) -> sum = sum + current);
            daysNumber = daysByWeek.size();
        }

        weekDto.setTotalBasicHours(totalBasicHours / 60);
        weekDto.setTotalNightHours(totalNightHours / 60);
        weekDto.setTotalBonusHours(totalBonusHours / 60);
        weekDto.setTotalLocationHours(totalBasicHours / 60);
        weekDto.setTotalMealAllowance(daysNumber * rateDto.getAllowance());
        weekDto.setBasicHoursIncome(BigDecimal.valueOf(totalBasicHours * rateDto.getHourlyRate()));
        weekDto.setNightHoursIncome(BigDecimal.valueOf(totalNightHours * rateDto.getNightRate()));
        weekDto.setBonusHoursIncome(BigDecimal.valueOf(totalBonusHours * rateDto.getOvertimeRate()));
        weekDto.setLocationHoursIncome(BigDecimal.valueOf(totalBasicHours * rateDto.getLocationRate()));
        return weekDto;
    }
}
