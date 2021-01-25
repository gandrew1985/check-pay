package com.andrzej.payroll.web.dtos;

import com.andrzej.payroll.persist.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkdayDto {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;
    private int weekNumber;
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime finishTime;
    @NotNull
    @PositiveOrZero
    private long deductionTime;
    private Duration totalPayableTime;
    private Duration totalNightHours;
    private Duration totalBonusHours;
    private BigDecimal beforeTaxIncome;
    private BigDecimal afterTaxIncome;
    private AppUser appUser;
}
