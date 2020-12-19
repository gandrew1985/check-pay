package com.andrzej.payroll.service;

import com.andrzej.payroll.domain.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@Service
public class TimeService {

    public Duration calculateTimeDiffDuration(LocalTime startTime, LocalTime finishTime, long deduction) {
        Duration resultDuration;

        if (finishTime.isBefore(startTime)) {
            Duration midnight = Duration.between(startTime, LocalTime.of(23, 59)).plusMinutes(1);
            Duration midToEnd = Duration.between(LocalTime.MIDNIGHT, finishTime);
            resultDuration = midnight.plus(midToEnd).minusMinutes(deduction);
        } else {
            resultDuration = Duration.between(startTime, finishTime).minusMinutes(deduction);
        }
        return resultDuration;
    }

    public AppUser getLoggedUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = (AppUser) object;
        return appUser;
    }

    public BigDecimal calculateBeforeTax(double rate, long time) {
        return BigDecimal.valueOf(rate / 60 * time);
    }

    public BigDecimal calculateAfterTax(double rate, long time) {
        double beforeTax = (rate / 60 * time);
        double tax = beforeTax * 0.2;
        return BigDecimal.valueOf(beforeTax-tax);
    }
}
