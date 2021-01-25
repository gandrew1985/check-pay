package com.andrzej.payroll.web.controller;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.web.dtos.RateDto;
import com.andrzej.payroll.web.dtos.WeekDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import com.andrzej.payroll.core.security.AppUserDetailsService;
import com.andrzej.payroll.core.logic.WorkdayCalculator;
import com.andrzej.payroll.persist.mapper.ServiceMapper;
import com.andrzej.payroll.persist.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class WorkdayController {

    private static final int ROW_PER_PAGE = 5;
    private final WorkdayService workdayService;
    private final WorkdayCalculator workdayCalculator;
    private final AppUserDetailsService userDetailsService;
    private final RateService rateService;
    private final ServiceMapper serviceMapper;
    private final WeekService weekService;

    @GetMapping("/workdays")
    public String getWorkdays(Model model,
                              @RequestParam(value = "page", defaultValue ="1") int pageNumber ) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        List<WorkdayDto> allWorkdaysDto = serviceMapper.mapToWorkdayDtoList(
                workdayService.findAllByAppUser(loggedUser,pageNumber,ROW_PER_PAGE));

        long count = workdayService.count();
        boolean hasPrevious = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;

        model.addAttribute("allDaysListDto", allWorkdaysDto);
        model.addAttribute("workdayDto", new WorkdayDto());
        model.addAttribute("hasPrev", hasPrevious);
        model.addAttribute("previous", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "workday-list";
    }

    @GetMapping("/workdays/add")
    public String showWorkday(Model model, Principal principal) {
        //Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        String details = RequestContextHolder.getRequestAttributes().getSessionId();
        LocalDateTime timeNow = LocalDateTime.now();
        model.addAttribute("name", principal.getName());
        model.addAttribute("details", details);
        model.addAttribute("workdayDto", new WorkdayDto());
        model.addAttribute("timeNow", timeNow);
        model.addAttribute("add",true);
        return "workday-edit";
    }

    @PostMapping("/workdays/addPost")
    public String saveWorkday(@ModelAttribute @Valid WorkdayDto workdayDto, WeekDto weekDto) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        RateDto rate = serviceMapper.mapToRateDto(rateService.findRateByUser(loggedUser));
        LocalDate dayProvided = workdayDto.getDate();//dzien podany przez usera
        if ((dayProvided.isAfter(LocalDate.now()) ||
                (workdayService.existWorkdayByDateAndUser(dayProvided, loggedUser)))) {
            return "error-workday"; //jezeli dayProvided istnieje w bazie to error
        }
        workdayDto = serviceMapper.mapToWorkdayDto(workdayCalculator.calculateWorkday(
                serviceMapper.mapToWorkday(workdayDto),
                serviceMapper.mapToRate(rate), loggedUser));
        workdayService.saveWorkday(serviceMapper.mapToWorkday(workdayDto));
        return "redirect:/workdays";
    }

    @GetMapping("/workdays/{workdayId}/edit")
    public String showEditWorkday(Model model, @PathVariable Long workdayId) {
        model.addAttribute("add", false);
        WorkdayDto editedWorkday = serviceMapper.mapToWorkdayDto(workdayService.findWorkdayById(workdayId));
        model.addAttribute("workdayDto", editedWorkday);
        return "workday-edit";
    }

    @PostMapping("/workdays/{workdayId}/edit")
    public String updateWorkday(Model model, @PathVariable Long workdayId,
                                @Valid @ModelAttribute WorkdayDto workdayDto,WeekDto weekDto) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        model.addAttribute("add", false);
        workdayDto.setId(workdayId);
        RateDto rate = serviceMapper.mapToRateDto(rateService.findRateByUser(loggedUser));

        workdayDto = serviceMapper.mapToWorkdayDto(workdayCalculator.calculateWorkday(
                serviceMapper.mapToWorkday(workdayDto),
                serviceMapper.mapToRate(rate), loggedUser));
        workdayService.updateWorkday(serviceMapper.mapToWorkday(workdayDto));
        return "redirect:/workdays";
    }

    @GetMapping("/workdays/{workdayDate}")
    public String chooseDate(Model model, @ModelAttribute WorkdayDto workdayDto,
                             @PathVariable LocalDate workdayDate) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        workdayDto.setDate(workdayDto.getDate());
        boolean existByDateAndUser = workdayService.existWorkdayByDateAndUser(workdayDate, loggedUser);

        if (existByDateAndUser) {
            WorkdayDto workdayDtoByDate = serviceMapper.
                    mapToWorkdayDto(workdayService.getWorkdayByDateAndUser(workdayDate, loggedUser));
            model.addAttribute("workdayByDate", workdayDtoByDate);
            return "workday-by-date";
        }
        return "redirect:/workdays";
    }

    @GetMapping("/workdays/{workdayId}/delete")
    public String deleteWorkday(@PathVariable Long workdayId) {
        workdayService.deleteWorkday(workdayId);
        return "redirect:/workdays";
    }
}