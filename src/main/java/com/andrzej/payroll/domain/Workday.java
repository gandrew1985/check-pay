package com.andrzej.payroll.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workdays")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime finishTime;
    private long deductionTime;
    private Duration totalPayableTime;
    private BigDecimal beforeTaxIncome;
    private BigDecimal afterTaxIncome;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    @OneToOne
    @JoinColumn(name = "rate_id", referencedColumnName = "id")
    public Rate rate;

    @Override
    public String toString() {
        return "Workday{" +
                "id=" + id +
                ", date=" + date +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", deductionTime=" + deductionTime +
                ", totalPayableTime=" + totalPayableTime +
                ", beforeTaxIncome=" + beforeTaxIncome +
                ", afterTaxIncome=" + afterTaxIncome +
                ", appUser=" + appUser +
                ", rate=" + rate +
                '}';
    }
}
