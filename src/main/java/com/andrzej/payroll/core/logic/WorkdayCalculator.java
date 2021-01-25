package com.andrzej.payroll.core.logic;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Rate;
import com.andrzej.payroll.persist.entity.Workday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class WorkdayCalculator {

    private final TimeCalculator timeCalculator;

    public BigDecimal calculateBeforeTax(Rate rate, Workday workday) {
        Duration totalWorkTime = timeCalculator.calculateTimeDiffDuration(workday.getStartTime(),
                workday.getFinishTime());
        BigDecimal nightBonus = timeCalculator.calculateBonus(rate.getNightRate(),
                timeCalculator.calculateNightHours(workday.getStartTime(), workday.getFinishTime()).toMinutes());
        BigDecimal locationBonus = timeCalculator.calculateBonus(rate.getLocationRate(), totalWorkTime.toMinutes());
        BigDecimal overtimeBonus = timeCalculator.calculateBonus(rate.getOvertimeRate(),
                timeCalculator.calculateOvertimeHours(workday.getDate(), workday.getStartTime(),
                        workday.getFinishTime()).toMinutes());
        BigDecimal basicPay = timeCalculator.calculateBonus(rate.getHourlyRate(),
                totalWorkTime.toMinutes());
        return BigDecimal.ZERO.add(nightBonus).add(locationBonus).add(overtimeBonus).add(basicPay)
                .add(BigDecimal.valueOf(rate.getAllowance()));
    }

    public BigDecimal calculateAfterTax(Rate rate, Workday workday) {
        BigDecimal beforeTax = calculateBeforeTax(rate, workday);
        BigDecimal tax = BigDecimal.valueOf(0.2);
        BigDecimal taxAmount = beforeTax.multiply(tax);
        return beforeTax.subtract(taxAmount);
    }

    public Workday calculateWorkday(Workday workday, Rate rate, AppUser loggedUser) {
        Duration totalWorkTime = timeCalculator.calculateTimeDiffDuration(workday.getStartTime(),
                workday.getFinishTime());
        Duration totalNightHours = timeCalculator.calculateNightHours(workday.getStartTime(),
                workday.getFinishTime());
        Duration totalBonusHours = timeCalculator.calculateBankHolidayHours(workday.getDate(),
                workday.getStartTime(), workday.getFinishTime()).plus(timeCalculator.calculateOvertimeHours(
                workday.getDate(), workday.getStartTime(), workday.getFinishTime()));
        BigDecimal incomeBeforeTax = calculateBeforeTax(rate, workday);
        BigDecimal incomeAfterTax = calculateAfterTax(rate, workday);
        int weekNumber = timeCalculator.getWeekOfYear(workday.getDate());

        workday.setWeekNumber(weekNumber);
        workday.setTotalPayableTime(totalWorkTime);
        workday.setTotalNightHours(totalNightHours);
        workday.setTotalBonusHours(totalBonusHours);
        workday.setAppUser(loggedUser);
        workday.setBeforeTaxIncome(incomeBeforeTax);
        workday.setAfterTaxIncome(incomeAfterTax);
        return workday;
    }
}