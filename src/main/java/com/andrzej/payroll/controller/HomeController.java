package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.mapper.ServiceMapper;
import com.andrzej.payroll.repository.WorkdayRepository;
import com.andrzej.payroll.service.TimeService;
import com.andrzej.payroll.service.UserDbService;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@EnableWebSecurity
public class HomeController {

    private TimeService timeService;
    private UserDbService userDbService;
    private ServiceMapper serviceMapper;
    private WorkdayRepository workdayRepository;

    public HomeController(TimeService timeService, UserDbService userDbService,
                          ServiceMapper serviceMapper, WorkdayRepository workdayRepository) {
        this.timeService = timeService;
        this.userDbService = userDbService;
        this.serviceMapper = serviceMapper;
        this.workdayRepository = workdayRepository;
    }

    @GetMapping("/getForm")
    public String getForm(Model model, Principal principal) {
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

    @PostMapping("/sendCalculate")
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

        System.out.println(workDay.getTotalPayableTime());
        System.out.println(workDay.getBeforeTaxIncome());
        System.out.println("rata:" + rate.getHourlyRate());
        System.out.println("home" + workDay.getAfterTaxIncome());
        return "redirect:/showAllDays";
    }
}