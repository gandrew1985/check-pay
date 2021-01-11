package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.RateDto;
import com.andrzej.payroll.domain.WorkdayDto;
import com.andrzej.payroll.mapper.ServiceMapper;
import com.andrzej.payroll.service.DbService;
import com.andrzej.payroll.service.WorkdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/workdays")
public class WorkdayController {

    private final WorkdayService workdayService;
    private final DbService dbService;
    private final ServiceMapper serviceMapper;

    @GetMapping("/getWorkdays")
    public String showAll(Model model) {
        AppUser loggedUser = workdayService.getLoggedUser();
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
    public String saveWorkday(@ModelAttribute WorkdayDto workdayDto) {
        AppUser loggedUser = workdayService.getLoggedUser();
        RateDto rate = serviceMapper.mapToRateDto(dbService.findRateByUser(loggedUser));
        LocalDate dayProvided = workdayDto.getDate();//dzien podany przez usera
        if ((dayProvided.isAfter(LocalDate.now()) ||
                (dbService.existWorkdayByDateAndUser(dayProvided, loggedUser)))) {
            return "error"; //jezeli dayProvided istnieje w bazie to error
        }
        workdayDto = serviceMapper.mapToWorkdayDto(workdayService.calculateWorkday(
                serviceMapper.mapToWorkday(workdayDto),
                serviceMapper.mapToRate(rate), loggedUser));
        dbService.saveWorkday(serviceMapper.mapToWorkday(workdayDto));
        return "redirect:/workdays/getWorkdays";
    }

    @GetMapping("/editWorkday/{id}")
    public String editWorkday(Model model, @PathVariable("id") Long id) {
        WorkdayDto dbDayDto = serviceMapper.mapToWorkdayDto(dbService.findWorkdayById(id));
        model.addAttribute("workdayDto", dbDayDto);
        return "editWorkday";
    }

    @PostMapping("/updateWorkday/{id}")
    public String updateWorkday(@PathVariable("id") Long id, @ModelAttribute WorkdayDto workdayDto) {
        AppUser loggedUser = workdayService.getLoggedUser();
        RateDto rate = serviceMapper.mapToRateDto(dbService.findRateByUser(loggedUser));

        workdayDto = serviceMapper.mapToWorkdayDto(workdayService.calculateWorkday(
                serviceMapper.mapToWorkday(workdayDto),
                serviceMapper.mapToRate(rate), loggedUser));
        dbService.updateWorkday(serviceMapper.mapToWorkday(workdayDto));
        return "redirect:/workdays/getWorkdays";
    }

    @GetMapping("/getWorkday")
    public String chooseDate(Model model, @ModelAttribute WorkdayDto workdayDto) {
        AppUser loggedUser = workdayService.getLoggedUser();
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