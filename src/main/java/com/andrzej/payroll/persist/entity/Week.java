package com.andrzej.payroll.persist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "weeks")
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    @OneToMany(mappedBy = "week")
    private List<Workday> workdayList;
}
