package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.domain.Rate;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.service.TimeService;
import com.andrzej.payroll.service.UserDbService;
import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkdayController {

    @Autowired
    private TimeService timeService;
    @Autowired
    private UserDbService userDbService;

    @PostMapping("/addWorkday")
    public String addWorkday(@RequestBody Workday workday) {
        return "redirect:/getForm";
    }

    @GetMapping("/editWorkday")
    public String editWorkday(Model model,@RequestParam Long id) {
        model.addAttribute("action", "/saveEditWorkday/" + id);
        model.addAttribute("header", "Edit Workday");
        Workday dbDay = userDbService.findWorkdayById(id);
        model.addAttribute("workday",dbDay);
        System.out.println("view" + dbDay);
        return "workday";
    }

    @PostMapping("/saveEditWorkday/{id}")
    public String saveEditedWorkday(@PathVariable Long id, @ModelAttribute Workday workday){
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
        System.out.println(workday.getTotalPayableTime());
        System.out.println(workday.getBeforeTaxIncome());
        System.out.println("rata:" + rate.getHourlyRate());
        System.out.println("home" + workday.getAfterTaxIncome());
        System.out.println("edited" + workday);
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

    @GetMapping("/showAllDays")
    public String showAll(Model model,Workday workday) {

        AppUser appUser = timeService.getLoggedUser();
        List<Workday> allWorkdays = userDbService.findAllByAppUser(appUser);
        model.addAttribute("allDaysList", allWorkdays);
        model.addAttribute("workday", new Workday());
        return "allDays";
    }

    @RequestMapping("/deleteWorkday")
    public String deleteWorkday(@RequestParam Long id) {
        userDbService.deleteWorkday(id);
        return "redirect:/showAllDays";
    }
}
