package com.andrzej.payroll.persist.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workdays")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private int weekNumber;
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private Week week;

    @Override
    public String toString() {
        return "Workday{" +
                "id=" + id +
                ", date=" + date +
                ", weekNumber=" + weekNumber +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", deductionTime=" + deductionTime +
                ", totalPayableTime=" + totalPayableTime +
                ", totalNightHours=" + totalNightHours +
                ", totalBonusHours=" + totalBonusHours +
                ", beforeTaxIncome=" + beforeTaxIncome +
                ", afterTaxIncome=" + afterTaxIncome +
                '}';
    }
}
