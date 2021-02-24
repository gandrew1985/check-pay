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

    /*REVIEW
    W tego typu dylematach musisz sie zastanowić czy jak bedą wprowadzane kolejne wolne dni do aplikacji.
    Nie jest dobrze jeżeli jest potrzebny programista do tego żeby updatować dane domenowe w apce.
    Celem jest by aplikacja po oddaniu do klienta była samodzielna i zmiany takie jak poniżej były
    wprowadzane przez klienta. Wiec najlepiej byłoby dodać endpoint na operacje na dniach wolnych.
    Poniższe rozwiązanie jest ok dla kwestii technicznych takich jak timeouty, URLe chociaż takie rzeczy raczej
    powinny lądować w propertisach lub dedykowanych plikach. Aplikacja instalowana na innym kompie powinna zawierać
    wbudowaną baze danych która przy zamknieciu aplikacaji jest convertowana do pliku który jest następnie odczytywany
    przy starcie. Gdy apki instalowane na serwerach lub w chmurze, bazy danych też tam są.
    Jeżeli martwisz się ze po starcie aplikacji nie masz tych danych w bazie to od tego jest schema sql za pomocą której
    tworzysz tabele i możesz też tam lokować dane. Zainteresuj sie też biblioteką Flyway która umożliwia zarządzaniem
    wersjami scheamy i tworzy strukture bazodanową przy starcie aplikacji.
    Tak BTW to obecnie nadal nie moge odpalić Twojej apki bez wygenerowania sobie modelu bazodanowego. Propertisy które
    umieściłeś mają namiar na baze na Twoim kompie i nigdzie indziej ta apka nie wstanie.
    Możesz to rozwiązać na 2 sposoby:
    1. Dodanie bazy danych embeded (np. h2) i namiarów na nią w domyślnych propertisach, oraz wspomnianej wyżej scheamy.
    Taka baza tworzyłaby się jedynie na czas życia aplikacji. Problemem jest tu różnica w dialekcie SQL pomiędzy MySQL a H2,
    co wiązałoby sie z utrzymywaniem dwóch scheam.
    2. Dodanie dockera z serverem MySQL oraz docker compose który stawiałby tą bazę w odpowiedniej konfiguracji oraz
    instrukcja w README jak uruchamiać aplikacje. docker-compose mógłby równiez stawiać przyokazji Twoją apkę jakbyś dodał
    do niej dockerfile. To na pewno bardziej skomplikowany sposób.
    */

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
