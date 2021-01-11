package com.andrzej.payroll.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkdayDto {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime finishTime;
    private long deductionTime;
    private Duration totalPayableTime;
    private Duration totalNightHours;
    private Duration totalBonusHours;
    private BigDecimal beforeTaxIncome;
    private BigDecimal afterTaxIncome;
    private AppUser appUser;
}
