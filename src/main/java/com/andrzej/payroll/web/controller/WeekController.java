package com.andrzej.payroll.web.controller;

import com.andrzej.payroll.core.security.AppUserDetailsService;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.mapper.ServiceMapper;
import com.andrzej.payroll.persist.service.WeekService;
import com.andrzej.payroll.persist.service.WorkdayService;
import com.andrzej.payroll.web.dtos.WeekDto;
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

    private final WeekService weekService;
    private final ServiceMapper serviceMapper;
    private final WorkdayService workdayService;
    private final AppUserDetailsService userDetailsService;

    @GetMapping("/weeks")
    public String showWeeks(Model model, WeekDto weekDto) {
        List<WeekDto> weekDtoList = serviceMapper.mapToWeekDtoList(weekService.findAllWeeks());
        model.addAttribute("weekDto", weekDto);
        model.addAttribute("weekDtoList", weekDtoList);
        return "week-list";
    }

    @GetMapping("/weeks/addGet")
    public String editWeek(Model model, WeekDto weekDto) {
        model.addAttribute("add", true);
        model.addAttribute("weekDto", weekDto);
        return "week-edit";
    }

    @PostMapping("/weeks/add")
    public String saveWeek(Model model, @ModelAttribute WeekDto weekDto) {

        AppUser loggedUser = userDetailsService.getLoggedUser();
        weekDto.setAppUser(loggedUser);
        boolean weekExist = weekService.existWeekForUserWithNumber(loggedUser, weekDto.getWeekNumber());
        if (weekExist) {
            return "error-week";
        }
        weekService.saveWeek(serviceMapper.mapToWeek(weekDto));

        return "redirect:/workdays";
    }

    @GetMapping("/weeks/{weekId}/edit")
    public String showEditWeeks(@PathVariable Long weekId, Model model) {
        model.addAttribute("add", false);
        //RateDto editedRates = serviceMapper.mapToRateDto(rateService.findRateById(rateId));
        //model.addAttribute("rateDto", editedRates);
        return "rates-edit";
    }

    @PostMapping("/weeks/{weekId}/edit")
    public String updateWeek(Model model, @PathVariable long weekId,
                             @Valid @ModelAttribute("weekDto") WeekDto weekDto) {
        model.addAttribute("add", false);
        weekDto.setId(weekId);
        // serviceMapper.mapToRateDto(rateService.updateRate(serviceMapper.mapToRate(rateDto)));
        return "redirect:/rates";
    }

    @GetMapping("/weeks/{weekId}/delete")
    public String deleteWeek(@PathVariable Long weekId) {
        // rateService.deleteRateById(rateId);
        return "week-list";
    }
}
