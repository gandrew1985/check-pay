package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.repository.WorkdayRepository;
import com.andrzej.payroll.service.TimeService;
import com.andrzej.payroll.service.UserDbService;
import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkdayController {

    private TimeService timeService;
    private UserDbService userDbService;
    private WorkdayRepository workdayRepository;

    public WorkdayController(TimeService timeService, UserDbService userDbService,
                             WorkdayRepository workdayRepository) {
        this.timeService = timeService;
        this.userDbService = userDbService;
        this.workdayRepository = workdayRepository;
    }

    @GetMapping("/showAllDays")
    public String showAll(Model model,Workday workday) {

        AppUser appUser = timeService.getLoggedUser();
        List<Workday> allWorkdays = userDbService.findAllByAppUser(appUser);
        model.addAttribute("allDaysList", allWorkdays);
        model.addAttribute("workday", new Workday());
        return "allDays";
    }

    @GetMapping("/addWorkday")
    public String showForm(Model model, Principal principal) {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        LocalDateTime timeNow = LocalDateTime.now();
        model.addAttribute("name", principal.getName());
        model.addAttribute("details", details);
        model.addAttribute("workday", new Workday());
        model.addAttribute("action", "/sendCalculate");
        model.addAttribute("header", "Add workday");
        model.addAttribute("timeNow", timeNow);
        return "workday";
    }

    @PostMapping("/saveWorkday")
    public String calculateTime(Model model, @ModelAttribute Workday workDay) {
        AppUser user = timeService.getLoggedUser();
        Rate rate = userDbService.findRateByUser(user);
        LocalDate dayProvided = workDay.getDate();//dzien podany przez usera
        if ((dayProvided.isAfter(LocalDate.now()) ||
                (userDbService.existWorkdayByDateAndUser(dayProvided,user))))  {
            return "error"; //jezeli dayProvided istnieje w bazie to error
        }
        Duration totalWorkTime = timeService.calculateTimeDiffDuration(workDay.getStartTime(),
                workDay.getFinishTime(), workDay.getDeductionTime());
        BigDecimal incomeBeforeTax = timeService.calculateBeforeTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());
        BigDecimal incomeAfterTax = timeService.calculateAfterTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());

        workDay.setTotalPayableTime(totalWorkTime);
        workDay.setAppUser(user);
        workDay.setBeforeTaxIncome(incomeBeforeTax);
        workDay.setAfterTaxIncome(incomeAfterTax);

        model.addAttribute("totalWorkTime", totalWorkTime);
        model.addAttribute("beforeTax", incomeBeforeTax);
        model.addAttribute("afterTax", incomeAfterTax);
        workdayRepository.save(workDay);
        return "redirect:/showAllDays";
    }

    @GetMapping("/editWorkday/{id}")
    public String editWorkday(Model model,@PathVariable("id") Long id) {
        Workday dbDay = userDbService.findWorkdayById(id);
        model.addAttribute("workday",dbDay);
        System.out.println("view" + dbDay);
        return "editWorkday";
    }

    @PostMapping("/updateWorkday/{id}")
    public String saveEditedWorkday(@PathVariable("id") Long id, @ModelAttribute Workday workday){
        AppUser user = timeService.getLoggedUser();
        Rate rate = userDbService.findRateByUser(user);
        Duration totalWorkTime = timeService.calculateTimeDiffDuration(workday.getStartTime(),
                workday.getFinishTime(), workday.getDeductionTime());
        BigDecimal incomeBeforeTax = timeService.calculateBeforeTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());
        BigDecimal incomeAfterTax = timeService.calculateAfterTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());

        workday.setTotalPayableTime(totalWorkTime);
        workday.setAppUser(user);
        workday.setBeforeTaxIncome(incomeBeforeTax);
        workday.setAfterTaxIncome(incomeAfterTax);
        userDbService.saveWorkday(workday);
        return "redirect:/showAllDays";
    }

    @GetMapping("/chooseDate")
    public String chooseDate(Model model, @ModelAttribute Workday workday) {
        LocalDate date = workday.getDate();
        AppUser appUser = timeService.getLoggedUser();
        boolean existByDateAndUser = userDbService.existWorkdayByDateAndUser(date, appUser);

        if (existByDateAndUser) {
            Workday workdayByDate = userDbService.getWorkdayByDateAndUser(date, appUser);
            model.addAttribute("workdayByDate", workdayByDate);
            return "dayByDate";
        }
        return "redirect:/getForm";
    }

    @GetMapping("/deleteWorkday/{id}")
    public String deleteWorkday(@PathVariable("id") Long id) {
        userDbService.deleteWorkday(id);
        return "redirect:/showAllDays";
    }
}