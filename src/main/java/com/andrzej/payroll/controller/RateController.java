package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.RateDto;
import com.andrzej.payroll.mapper.ServiceMapper;
import com.andrzej.payroll.service.DbService;
import com.andrzej.payroll.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rates")
public class RateController {

    private final ServiceMapper serviceMapper;
    private final TimeService timeService;
    private final DbService dbService;

    @GetMapping("/getRates")
    public String showRates(Model model, Principal principal) {
        AppUser loggedUser = timeService.getLoggedUser();
        List<RateDto> ratesList = serviceMapper.mapToListRateDto(dbService.findAllRatesByAppUser(loggedUser));
        model.addAttribute("rateDto", new RateDto());
        model.addAttribute("ratesList", ratesList);
        model.addAttribute("name", principal.getName());
        return "showRates";
    }

    @GetMapping("/addRates")
    public String addRate(Model model, RateDto rateDto) {
        model.addAttribute("rateDto", rateDto);
        return "addRates";
    }

    @PostMapping("/createRates")
    public String saveRate(@ModelAttribute RateDto rateDto) {
        AppUser loggedUser = timeService.getLoggedUser();
        rateDto.setAppUser(timeService.getLoggedUser());
        boolean rates = dbService.existRateForUser(loggedUser);

        if (rates) {
            return "errorRate";
        }
        dbService.createRate(serviceMapper.mapToRate(rateDto));
        return "redirect:/rates/getRates";
    }

    @GetMapping("/editRates/{id}")
    public String editRates(@PathVariable Long id, Model model) {
        RateDto dbRates = serviceMapper.mapToRateDto(dbService.findRateById(id));
        model.addAttribute("rateDto", dbRates);
        return "editRates";
    }

    @PostMapping("/updateRates/{id}")
    public String updateRate(@ModelAttribute RateDto rateDto) {
        serviceMapper.mapToRateDto(dbService.updateRate(serviceMapper.mapToRate(rateDto)));
        return "redirect:/rates/getRates";
    }

    @GetMapping("/deleteRates/{id}")
    public String deleteRate(@PathVariable Long id) {
        dbService.deleteRateById(id);
        return "redirect:/rates/getRates";
    }
}
