package com.andrzej.payroll.web.controller;

import com.andrzej.payroll.core.logic.WeekCalculator;
import com.andrzej.payroll.core.security.AppUserDetailsService;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.mapper.ServiceMapper;
import com.andrzej.payroll.persist.service.RateService;
import com.andrzej.payroll.persist.service.WeekService;
import com.andrzej.payroll.persist.service.WorkdayService;
import com.andrzej.payroll.web.dtos.RateDto;
import com.andrzej.payroll.web.dtos.WeekDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WeekController {

    /*Musialem tu wszedzie dac Requesty GET bo jak dawalem Post tam gdzie powinny one byc,
    * to wyskakiwal mi blad ze HTTP Request "Get" not supported... a jak zmienilem na Get
    * to dziala
     */

    private final WeekService weekService;
    private final ServiceMapper serviceMapper;
    private final WeekCalculator weekCalculator;
    private final RateService rateService;
    private final WorkdayService workdayService;
    private final AppUserDetailsService userDetailsService;

    @GetMapping("/weeks")
    public String showWeeks(Model model, WeekDto weekDto) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        String weekStr = "Week #";
        List<WeekDto> weekDtoList = serviceMapper.mapToWeekDtoList(weekService.findAllByAppUser(loggedUser));
        model.addAttribute("weekStr", weekStr);
        model.addAttribute("weekDto", weekDto);
        model.addAttribute("weekDtoList", weekDtoList);
        return "week-list";
    }

    @GetMapping("/weeks/{weekNumber}")
    public String showWeek(@PathVariable int weekNumber, Model model, @ModelAttribute WeekDto weekDto) {
        AppUser appUser = userDetailsService.getLoggedUser();
        boolean weekExist = weekService.existWeekForUserWithNumber(appUser, weekNumber);
        if (weekExist) {
            WeekDto weekByNumber = serviceMapper
                    .mapToWeekDto(weekService.findWeekByUserAndNumber(appUser, weekNumber));
            model.addAttribute("weekDtoByNumber", weekByNumber);
            System.out.println(weekByNumber);
            return "week-by-number";
        }
        return "redirect:/workdays";
    }

    @GetMapping("/weeks/{weekNumber}/add")
    public String addWeek(@ModelAttribute("weekDto") @Valid WeekDto weekDto, @PathVariable int weekNumber) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        boolean weekExist = weekService.existWeekForUserWithNumber(loggedUser, weekNumber);
        RateDto rateDto = serviceMapper.mapToRateDto(rateService.findRateByUser(loggedUser));
        List<WorkdayDto> daysByWeek = serviceMapper.mapToWorkdayDtoList(workdayService
                .getAllWorkdaysByWeekNumber(weekNumber));
        if (weekExist) {
            return "redirect:/weeks/{weekNumber}/edit";
        }
        weekDto = weekCalculator.calculateWeek(daysByWeek, rateDto, weekNumber, weekDto, loggedUser);
        weekService.saveWeek(serviceMapper.mapToWeek(weekDto));
        return "redirect:/workdays";
    }

    @GetMapping("/weeks/{weekNumber}/edit")
    public String updateWeek(Model model, @PathVariable int weekNumber,
                             @Valid @ModelAttribute WeekDto weekDto) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        weekDto.setWeekNumber(weekNumber);
        RateDto rate = serviceMapper.mapToRateDto(rateService.findRateByUser(loggedUser));
        List<WorkdayDto> daysByWeek = serviceMapper.mapToWorkdayDtoList(workdayService
                .getAllWorkdaysByWeekNumber(weekNumber));
        weekDto = weekCalculator.calculateWeek(daysByWeek, rate, weekNumber, weekDto, loggedUser);
        weekService.updateWeek(serviceMapper.mapToWeek(weekDto));
        return "redirect:/weeks/{weekNumber}";
    }

    @PostMapping("/weeks/{weekId}/delete")
    public String deleteWeek(@PathVariable Long weekId) {
        weekService.deleteWeek(weekId);
        return "week-list";
    }
}
