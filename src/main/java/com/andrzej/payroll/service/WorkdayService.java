package com.andrzej.payroll.service;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.Workday;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class WorkdayService {

    private final TimeCalculation timeCalculation;
    /* MOglbys sie przypatrzyc tej klasie, strasznie duzo mam tu tych samych powtorzen w metodach...
    myslalem o stalej jak ponizej, ale nie wiem czy tak mozna :P
   private static final double NIGHT_RATE = new Rate().getNightRate();
   jakies sugestie zeby troche ten kod lepiej wygladal??
     */

    public AppUser getLoggedUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (AppUser) object;
    }

    public BigDecimal calculateBeforeTax(Rate rate, Workday workday) {

        Duration totalWorkTime = timeCalculation.calculateTimeDiffDuration(workday.getStartTime(),
                workday.getFinishTime());

        BigDecimal nightBonus = timeCalculation.calculateBonus(rate.getNightRate(),
                timeCalculation.calculateNightHours(workday.getStartTime(), workday.getFinishTime()).toMinutes());
        BigDecimal locationBonus = timeCalculation.calculateBonus(rate.getLocationRate(), totalWorkTime.toMinutes());
        BigDecimal overtimeBonus = timeCalculation.calculateBonus(rate.getOvertimeRate(),
                timeCalculation.calculateOvertimeHours(workday.getDate(), workday.getStartTime(),
                        workday.getFinishTime()).toMinutes());
        BigDecimal basicPay = timeCalculation.calculateBonus(rate.getHourlyRate(),
                totalWorkTime.toMinutes());
        return BigDecimal.ZERO.add(nightBonus).add(locationBonus).add(overtimeBonus).add(basicPay);
    }

    public BigDecimal calculateAfterTax(Rate rate, Workday workday) {
        BigDecimal beforeTax = calculateBeforeTax(rate, workday);
        BigDecimal tax = BigDecimal.valueOf(0.2);
        BigDecimal taxAmount = beforeTax.multiply(tax);
        return beforeTax.subtract(taxAmount);
    }

    public Workday calculateWorkday(Workday workday, Rate rate, AppUser loggedUser) {

        Duration totalWorkTime = timeCalculation.calculateTimeDiffDuration(workday.getStartTime(),
                workday.getFinishTime());
        Duration totalNightHours = timeCalculation.calculateNightHours(workday.getStartTime(),
                workday.getFinishTime());
        Duration totalBonusHours = timeCalculation.calculateBankHolidayHours(workday.getDate(),
                workday.getStartTime(), workday.getFinishTime()).plus(timeCalculation.calculateOvertimeHours(
                workday.getDate(), workday.getStartTime(), workday.getFinishTime()));
        BigDecimal incomeBeforeTax = calculateBeforeTax(rate, workday);
        BigDecimal incomeAfterTax = calculateAfterTax(rate, workday);

        workday.setTotalPayableTime(totalWorkTime);
        workday.setTotalNightHours(totalNightHours);
        workday.setTotalBonusHours(totalBonusHours);
        workday.setAppUser(loggedUser);
        workday.setBeforeTaxIncome(incomeBeforeTax);
        workday.setAfterTaxIncome(incomeAfterTax);
        return workday;
    }
}
