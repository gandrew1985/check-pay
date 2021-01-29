package com.andrzej.payroll.core.logic;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.web.dtos.RateDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class WorkdayCalculator {

    /* Tak samo tutaj nie wiem czy ta klasa jest do konca poprawna
    * bo te metody troche obszerne mi sie wydaja, ale za bardzo nie mam pomyslu
    * jak je zwezic....
     */

    private final WorkdayTimeCalculator workdayTimeCalculator;

    public BigDecimal calculateBeforeTax(RateDto rateDto, WorkdayDto workdayDto) {
        Duration totalWorkTime = workdayTimeCalculator.calculateTimeDiffDuration(workdayDto.getStartTime(),
                workdayDto.getFinishTime());
        BigDecimal nightBonus = workdayTimeCalculator.calculateBonus(rateDto.getNightRate(),
                workdayTimeCalculator.calculateNightHours(workdayDto.getStartTime(), workdayDto.getFinishTime()).toMinutes());
        BigDecimal locationBonus = workdayTimeCalculator.calculateBonus(rateDto.getLocationRate(), totalWorkTime.toMinutes());
        BigDecimal overtimeBonus = workdayTimeCalculator.calculateBonus(rateDto.getOvertimeRate(),
                workdayTimeCalculator.calculateOvertimeHours(workdayDto.getDate(), workdayDto.getStartTime(),
                        workdayDto.getFinishTime()).toMinutes());
        BigDecimal basicPay = workdayTimeCalculator.calculateBonus(rateDto.getHourlyRate(),
                totalWorkTime.toMinutes());
        return BigDecimal.ZERO.add(nightBonus).add(locationBonus).add(overtimeBonus).add(basicPay)
                .add(BigDecimal.valueOf(rateDto.getAllowance()));
    }

    public BigDecimal calculateDeduction(BigDecimal beforeTaxIncome, RateDto rateDto) {
        BigDecimal tax = BigDecimal.valueOf(0.2);
        BigDecimal taxAmount = beforeTaxIncome.multiply(tax);
        BigDecimal pensionScheme = BigDecimal.valueOf(rateDto.getPensionScheme() / 100);
        BigDecimal pensionDeduct = beforeTaxIncome.multiply(pensionScheme);
        return taxAmount.add(pensionDeduct);
    }

    public WorkdayDto calculateWorkday(WorkdayDto workdayDto, RateDto rateDto, AppUser loggedUser) {
        Duration totalWorkTime = workdayTimeCalculator.calculateTimeDiffDuration(workdayDto.getStartTime(),
                workdayDto.getFinishTime());
        Duration totalNightHours = workdayTimeCalculator.calculateNightHours(workdayDto.getStartTime(),
                workdayDto.getFinishTime());
        Duration totalBonusHours = workdayTimeCalculator.calculateBankHolidayHours(workdayDto.getDate(),
                workdayDto.getStartTime(), workdayDto.getFinishTime())
                .plus(workdayTimeCalculator.calculateOvertimeHours(workdayDto.getDate(),
                        workdayDto.getStartTime(), workdayDto.getFinishTime()));
        BigDecimal incomeBeforeTax = calculateBeforeTax(rateDto, workdayDto);
        BigDecimal deduction = calculateDeduction(incomeBeforeTax, rateDto);
        BigDecimal incomeAfterTax = incomeBeforeTax.subtract(deduction);
        int weekNumber = workdayTimeCalculator.getWeekOfYear(workdayDto.getDate());

        workdayDto.setWeekNumber(weekNumber);
        workdayDto.setTotalPayableTime(totalWorkTime);
        workdayDto.setTotalNightHours(totalNightHours);
        workdayDto.setTotalBonusHours(totalBonusHours);
        workdayDto.setAppUser(loggedUser);
        workdayDto.setBeforeTaxIncome(incomeBeforeTax);
        workdayDto.setAfterTaxIncome(incomeAfterTax);
        return workdayDto;
    }
}