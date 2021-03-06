package com.andrzej.payroll.core.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@Data
public class BankHoliday {

    //nie wiem czy tak moge zrobic, czy nie powinienem umiescic ich w bazie danych??
    //jesli tak, to jak to najlepiej zrobic?
    //stworzyc tabele bezposrednio  w mySql u z pominieciem Springa?
    //choc wtedy jak bedzie apka isntalowana na innym kompie to nie bedzie tych swiat....:P
    //czy jak stawiamy apke np w chmurze, to rowniez baza danych jest w chmurze, i apka z niej korzysta?

    private final Set<LocalDate> bankHolidaysSet = new HashSet<>();

    private BankHoliday() {
        bankHolidaysSet.add(LocalDate.of(2020, 1, 1));
        bankHolidaysSet.add(LocalDate.of(2020, 4, 10));
        bankHolidaysSet.add(LocalDate.of(2020, 4, 13));
        bankHolidaysSet.add(LocalDate.of(2020, 5, 8));
        bankHolidaysSet.add(LocalDate.of(2020, 5, 25));
        bankHolidaysSet.add(LocalDate.of(2020, 8, 31));
        bankHolidaysSet.add(LocalDate.of(2020, 12, 25));
        bankHolidaysSet.add(LocalDate.of(2020, 12, 28));
        bankHolidaysSet.add(LocalDate.of(2021, 1, 1));
        bankHolidaysSet.add(LocalDate.of(2021, 4, 2));
        bankHolidaysSet.add(LocalDate.of(2021, 4, 5));
        bankHolidaysSet.add(LocalDate.of(2021, 5, 3));
        bankHolidaysSet.add(LocalDate.of(2021, 5, 31));
        bankHolidaysSet.add(LocalDate.of(2021, 8, 30));
        bankHolidaysSet.add(LocalDate.of(2021, 12, 27));
        bankHolidaysSet.add(LocalDate.of(2021, 12, 28));
    }
}
