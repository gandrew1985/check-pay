package com.andrzej.payroll.core.logic;

import com.andrzej.payroll.core.domain.BankHoliday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class WorkdayTimeCalculator {

    private static final LocalTime START_NIGHT = LocalTime.of(20, 00);
    private static final LocalTime END_NIGHT = LocalTime.of(06, 00);
    private final BankHoliday bankHoliday;

    public static int getDayOfWeek(LocalDate date) {
        //Monday is 1 and Sunday is 7
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    public int getWeekOfYear(LocalDate date) {
        int weekNumber = 0;
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 7);
        boolean dateBetween = isBetweenDate(date, LocalDate.of(2020, 12, 27),
                LocalDate.of(2021, 1, 2));
        if (dateBetween) {
            weekNumber = 53;
        } else if (date.isBefore(LocalDate.of(2020, 12, 27))) {
            weekNumber = date.get(weekFields.weekOfYear()) + 1;
        } else {
            weekNumber = date.get(weekFields.weekOfYear());
        }
        return weekNumber;
    }

    public boolean isBetweenDate(LocalDate date, LocalDate from, LocalDate to) {
        if (((date.isAfter(from)) || (date.equals(from))) &&
                (date.isBefore(to)) || (date.equals(to))) {
            return true;
        }
        return false;
    }

    public boolean isBetweenTime(LocalTime t, LocalTime from, LocalTime to) {
        if (((t.isAfter(from)) || (t.equals(from))) && (t.isBefore(to)) || (t.equals(to))) {
            return true;
        }
        return false;
    }

    public Duration calculateTimeDiffDuration(LocalTime startTime, LocalTime finishTime) {
        Duration resultDuration;

        if (finishTime.isBefore(startTime)) {
            Duration midnight = Duration.between(startTime, LocalTime.of(23, 59)).plusMinutes(1);
            Duration midToEnd = Duration.between(LocalTime.MIDNIGHT, finishTime);
            resultDuration = midnight.plus(midToEnd);
        } else {
            resultDuration = Duration.between(startTime, finishTime);
        }
        return resultDuration;
    }

    public Duration calculateBankHolidayHours(LocalDate date, LocalTime startTime, LocalTime finishTime) {
        Set<LocalDate> bankHolidays = bankHoliday.getBankHolidaysSet();
        while (bankHolidays.contains(date)) {
            if (startTime.isBefore(finishTime)) {
                return calculateTimeDiffDuration(startTime, finishTime);
            } else {
                return calculateTimeDiffDuration(startTime, LocalTime.MIDNIGHT);
            }
        }
        return Duration.ZERO;
    }

    public BigDecimal calculateBonus(double rate, double time) {
        return BigDecimal.valueOf(rate / 60 * time);
    }

    public Duration calculateOvertimeHours(LocalDate date, LocalTime startTime, LocalTime finishTime) {
        if ((getDayOfWeek(date) == 5) && (startTime.isAfter(finishTime))) {
            return calculateTimeDiffDuration(LocalTime.MIDNIGHT, finishTime);
        } else if (((getDayOfWeek(date) == 6)) || (((getDayOfWeek(date) == 7)) &&
                ((startTime.isBefore(finishTime)) || (finishTime.equals(LocalTime.MIDNIGHT))))) {
            return calculateTimeDiffDuration(startTime, finishTime);
        } else if (getDayOfWeek(date) == 7) {
            return calculateTimeDiffDuration(startTime, LocalTime.MIDNIGHT);
        }
        return Duration.ZERO;
    }

    public Duration calculateNightHours(LocalTime startTime, LocalTime finishTime) {
        while (startTime.isBefore(finishTime) || startTime.equals(finishTime)) {
            if (((isBetweenTime(startTime, END_NIGHT, START_NIGHT)) && (isBetweenTime(finishTime, END_NIGHT, START_NIGHT)))
                    || startTime.equals(finishTime)) {
                return Duration.ZERO;
            } else {
                LocalTime overlapStartBeforeEndNight = startTime.isBefore(END_NIGHT) ? startTime : END_NIGHT;
                LocalTime overlapStartAfterNightStart = startTime.isAfter(START_NIGHT) ? startTime : START_NIGHT;
                LocalTime overlapEndAfterStartNight = finishTime.isAfter(START_NIGHT) ? finishTime : START_NIGHT;
                LocalTime overlapEndBeforeEndNight = finishTime.isBefore(END_NIGHT) ? finishTime : END_NIGHT;

                return Duration.between(overlapStartBeforeEndNight, END_NIGHT)
                        .plus(Duration.between(START_NIGHT, overlapEndAfterStartNight))
                        .plus(Duration.between(END_NIGHT, overlapEndBeforeEndNight))
                        .plus(Duration.between(overlapStartAfterNightStart, START_NIGHT));
            }
        }
        LocalTime overlapStartAfterStartNight = startTime.isAfter(START_NIGHT) ? startTime : START_NIGHT;
        LocalTime overlapEndBeforeNightEnd = finishTime.isAfter(END_NIGHT) ? END_NIGHT : finishTime;
        return Duration.between(overlapStartAfterStartNight, LocalTime.MIDNIGHT.minusMinutes(1)).plusMinutes(1)
                .plus(Duration.between(LocalTime.MIDNIGHT, overlapEndBeforeNightEnd));
    }
}