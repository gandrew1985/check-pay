package com.andrzej.payroll.core.logic;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.web.dtos.RateDto;
import com.andrzej.payroll.web.dtos.WeekDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekCalculator {

    // nie wiem czy moge te metody tutaj przeniesc, czy lepiej bedzie jak beda w controllerze?
    // a moze lepiej ja rozdzielic na kilka metod, tylko w sumie metoda ta  ma jedno zadanie
    // liczy Week....??
    //zrobilem tak bo w controlerze zarowno updateWeek i addWeek korzysta z tej metody....

    /*REVIEW
    Jest ok.
    Logika biznesowa nie powinna sie znajdować w Controllerze. Controller to jedynie bramka ze światem zewnętrznym
    i nie powinnien mieć innych odpowiedzialności. Jedyne d oczego mógłbym sie doczepić do ta metoda mogłby być podzielona
    na metody prywatne bo obecnie jest za długa. I w publicznej metodzie byś wywoływał te metody.
    Według mnie klasy i metody powinny być małe bo sie je lepiej czyta ale są różne szkoły.
     */
    private final WorkdayCalculator workdayCalculator;

    public WeekDto calculateWeek(List<WorkdayDto> daysByWeek, RateDto rateDto, int weekNr,
                                 WeekDto weekDto, AppUser loggedUser) {

        double totalBasicHours = daysByWeek.stream()
                .map(workdayDto -> workdayDto.getTotalPayableTime().toMinutes())
                .reduce(0L, (sum, current) -> sum = sum + current)
                .doubleValue() / 60;
        double totalNightHours = daysByWeek.stream()
                .map(workdayDto -> workdayDto.getTotalNightHours().toMinutes())
                .reduce(0L, (sum, current) -> sum = sum + current)
                .doubleValue() / 60;
        double totalBonusHours = daysByWeek.stream()
                .map(workdayDto -> workdayDto.getTotalBonusHours().toMinutes())
                .reduce(0L, (sum, current) -> sum = sum + current)
                .doubleValue() / 60;
        int daysNumber = daysByWeek.size();
        double mealAllowance = daysNumber * rateDto.getAllowance();
        BigDecimal basicHoursIncome = BigDecimal.valueOf(totalBasicHours * rateDto.getHourlyRate());
        BigDecimal nightHoursIncome = BigDecimal.valueOf(totalNightHours * rateDto.getNightRate());
        BigDecimal bonusHoursIncome = BigDecimal.valueOf(totalBonusHours * rateDto.getOvertimeRate());
        BigDecimal locationHoursIncome = BigDecimal.valueOf(totalBasicHours * rateDto.getLocationRate());
        BigDecimal totalIncome = basicHoursIncome.add(nightHoursIncome)
                .add(bonusHoursIncome).add(locationHoursIncome);
        BigDecimal deductions = workdayCalculator.calculateDeduction(totalIncome, rateDto);
        BigDecimal afterTaxIncome = totalIncome.subtract(deductions);
        System.out.println("deductions: " + deductions);

        weekDto.setWeekNumber(weekNr);
        weekDto.setAppUser(loggedUser);
        weekDto.setTotalBasicHours(totalBasicHours);
        weekDto.setTotalNightHours(totalNightHours);
        weekDto.setTotalBonusHours(totalBonusHours);
        weekDto.setTotalLocationHours(totalBasicHours);
        weekDto.setTotalMealAllowance(mealAllowance);
        weekDto.setBasicHoursIncome(basicHoursIncome);
        weekDto.setNightHoursIncome(nightHoursIncome);
        weekDto.setBonusHoursIncome(bonusHoursIncome);
        weekDto.setLocationHoursIncome(locationHoursIncome);
        weekDto.setTotalIncome(totalIncome);
        weekDto.setAfterTaxIncome(afterTaxIncome);
        return weekDto;
    }
}
