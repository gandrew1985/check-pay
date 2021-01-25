package com.andrzej.payroll.web.controller;

import com.andrzej.payroll.core.logic.WorkdayCalculator;
import com.andrzej.payroll.core.security.AppUserDetailsService;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.mapper.ServiceMapper;
import com.andrzej.payroll.persist.service.RateService;
import com.andrzej.payroll.web.dtos.RateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RateController {

    private final ServiceMapper serviceMapper;
    private final WorkdayCalculator workdayCalculator;
    private final AppUserDetailsService userDetailsService;
    private final RateService rateService;

    @GetMapping("/rates")
    public String showRates(Model model, Principal principal) {
        AppUser loggedUser = userDetailsService.getLoggedUser();
        List<RateDto> ratesList = serviceMapper.mapToListRateDto(
                rateService.findAllRatesByAppUser(loggedUser));
        model.addAttribute("rateDto", new RateDto());
        model.addAttribute("ratesList", ratesList);
        model.addAttribute("name", principal.getName());
        return "rates-list";
    }

    @GetMapping("/rates/add")
    public String addRate(Model model, RateDto rateDto) {
        model.addAttribute("rateDto", rateDto);
        model.addAttribute("add", true);
        return "rates-edit";
    }

    @PostMapping("/rates/add")
    public String saveRate(Model model, @Valid @ModelAttribute RateDto rateDto) {
        model.addAttribute("add", true);
        AppUser loggedUser = userDetailsService.getLoggedUser();
        rateDto.setAppUser(loggedUser);
        boolean rates = rateService.existRateForUser(loggedUser);

        if (rates) {
            return "error-rate";
        }
        rateService.createRate(serviceMapper.mapToRate(rateDto));
        return "redirect:/rates";
    }

    @GetMapping("/rates/{rateId}/edit")
    public String showEditRates(@PathVariable Long rateId, Model model) {
        model.addAttribute("add", false);
        RateDto editedRates = serviceMapper.mapToRateDto(rateService.findRateById(rateId));
        model.addAttribute("rateDto", editedRates);
        return "rates-edit";
    }

    @PostMapping("/rates/{rateId}/edit")
    public String updateRate(Model model,@PathVariable long rateId,
                             @Valid @ModelAttribute ("rateDto") RateDto rateDto) {
        model.addAttribute("add", false);
        rateDto.setId(rateId);
        serviceMapper.mapToRateDto(rateService.updateRate(serviceMapper.mapToRate(rateDto)));
        return "redirect:/rates";
    }

    @GetMapping("/rates/{rateId}/delete")
    public String deleteRate(@PathVariable Long rateId) {
        rateService.deleteRateById(rateId);
        return "rates-list";
    }
}
