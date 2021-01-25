package com.andrzej.payroll.web.dtos;

import com.andrzej.payroll.persist.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekDto {

    private Long id;
    private int weekNumber;
    private double totalBasicHours;
    private double totalBonusHours;
    private double totalNightHours;
    private double totalLocationHours;
    private double totalMealAllowance;
    private BigDecimal basicHoursIncome;
    private BigDecimal bonusHoursIncome;
    private BigDecimal nightHoursIncome;
    private BigDecimal locationHoursIncome;
    private BigDecimal totalIncome;
    private BigDecimal afterTaxIncome;
    private AppUser appUser;
    private List<WorkdayDto> workdayDtoList;
}
