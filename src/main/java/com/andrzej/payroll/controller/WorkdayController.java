package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.*;
import com.andrzej.payroll.mapper.ServiceMapper;
import com.andrzej.payroll.repository.WorkdayRepository;
import com.andrzej.payroll.service.DbService;
import com.andrzej.payroll.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/workdays")
public class WorkdayController {

    private final TimeService timeService;
    private final DbService dbService;
    private final ServiceMapper serviceMapper;

    @GetMapping("/getWorkdays")
    public String showAll(Model model) {
        AppUser loggedUser = timeService.getLoggedUser();
        List<WorkdayDto> allWorkdaysDto = serviceMapper.mapToWorkdayDtoList(dbService.findAllByAppUser(loggedUser));
        model.addAttribute("allDaysListDto", allWorkdaysDto);
        model.addAttribute("workdayDto", new WorkdayDto());
        return "allDays";
    }

    @GetMapping("/addWorkday")
    public String showForm(Model model, Principal principal) {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        LocalDateTime timeNow = LocalDateTime.now();
        model.addAttribute("name", principal.getName());
        model.addAttribute("details", details);
        model.addAttribute("workdayDto", new WorkdayDto());
        model.addAttribute("timeNow", timeNow);
        return "workday";
    }

    @PostMapping("/createWorkday")
    public String calculateTime(Model model, @ModelAttribute WorkdayDto workDayDto) {
        AppUser loggedUser = timeService.getLoggedUser();
        RateDto rate = serviceMapper.mapToRateDto(dbService.findRateByUser(loggedUser));
        LocalDate dayProvided = workDayDto.getDate();//dzien podany przez usera
        if ((dayProvided.isAfter(LocalDate.now()) ||
                (dbService.existWorkdayByDateAndUser(dayProvided, loggedUser)))) {
            return "error"; //jezeli dayProvided istnieje w bazie to error
        }
        Duration totalWorkTime = timeService.calculateTimeDiffDuration(workDayDto.getStartTime(),
                workDayDto.getFinishTime(), workDayDto.getDeductionTime());
        BigDecimal incomeBeforeTax = timeService.calculateBeforeTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());
        BigDecimal incomeAfterTax = timeService.calculateAfterTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());

        workDayDto.setTotalPayableTime(totalWorkTime);
        workDayDto.setAppUser(loggedUser);
        workDayDto.setBeforeTaxIncome(incomeBeforeTax);
        workDayDto.setAfterTaxIncome(incomeAfterTax);

        model.addAttribute("totalWorkTime", totalWorkTime);
        model.addAttribute("beforeTax", incomeBeforeTax);
        model.addAttribute("afterTax", incomeAfterTax);
        dbService.saveWorkday(serviceMapper.mapToWorkday(workDayDto));
        return "redirect:/workdays/getWorkdays";
    }

    @GetMapping("/editWorkday/{id}")
    public String editWorkday(Model model, @PathVariable("id") Long id) {
        WorkdayDto dbDayDto = serviceMapper.mapToWorkdayDto(dbService.findWorkdayById(id));
        model.addAttribute("workdayDto", dbDayDto);
        return "editWorkday";
    }

    @PostMapping("/updateWorkday/{id}")
    public String saveEditedWorkday(@PathVariable("id") Long id, @ModelAttribute WorkdayDto workdayDto) {
        AppUser loggedUser = timeService.getLoggedUser();
        Rate rate = dbService.findRateByUser(loggedUser);
        Duration totalWorkTime = timeService.calculateTimeDiffDuration(workdayDto.getStartTime(),
                workdayDto.getFinishTime(), workdayDto.getDeductionTime());
        BigDecimal incomeBeforeTax = timeService.calculateBeforeTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());
        BigDecimal incomeAfterTax = timeService.calculateAfterTax(rate.getHourlyRate(),
                totalWorkTime.toMinutes());

        workdayDto.setTotalPayableTime(totalWorkTime);
        workdayDto.setAppUser(loggedUser);
        workdayDto.setBeforeTaxIncome(incomeBeforeTax);
        workdayDto.setAfterTaxIncome(incomeAfterTax);
        dbService.saveWorkday(serviceMapper.mapToWorkday(workdayDto));
        return "redirect:/workdays/getWorkdays";
    }

    @GetMapping("/getWorkday")
    public String chooseDate(Model model, @ModelAttribute WorkdayDto workdayDto) {
        AppUser loggedUser = timeService.getLoggedUser();
        LocalDate date = workdayDto.getDate();
        boolean existByDateAndUser = dbService.existWorkdayByDateAndUser(date, loggedUser);

        if (existByDateAndUser) {
            WorkdayDto workdayDtoByDate = serviceMapper.
                    mapToWorkdayDto(dbService.getWorkdayByDateAndUser(date, loggedUser));
            model.addAttribute("workdayByDate", workdayDtoByDate);
            return "dayByDate";
        }
        return "redirect:/workdays/addWorkday";
    }

    @GetMapping("/deleteWorkday/{id}")
    public String deleteWorkday(@PathVariable("id") Long id) {
        dbService.deleteWorkday(id);
        return "redirect:/workdays/getWorkdays";
    }
}