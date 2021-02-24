package com.andrzej.payroll.persist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

        /*REVIEW
        Według mnie ta tabela ma za duzo kolumn.
        Masz tu dane dotyczące tygodnia, godzin, dochód,
        Widzę tu trzy tabele ktore mogłby być powiązane jedną UserWeek.
        Ale to troche rewolucja wiec możesz to potraktowac jako PoC jak bedziesz mieć czas.
        Podobnie w Workday.
         */
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
