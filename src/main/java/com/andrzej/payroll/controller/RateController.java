package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.RateDto;
import com.andrzej.payroll.mapper.ServiceMapper;
import com.andrzej.payroll.repository.RateRepository;
import com.andrzej.payroll.service.TimeService;
import com.andrzej.payroll.service.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class RateController {

    /*
     * REVIEW
     * Nigdy nie wstrzykuj przez fieldy :)
     */

    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private TimeService timeService;
    @Autowired
    private UserDbService userDbService;

    @GetMapping("/getRate")
    public String showRate(Model model, Principal principal, RateDto rateDto) {
        model.addAttribute("rateId",rateDto.getId());
        model.addAttribute("newRates", new RateDto());
        model.addAttribute("name", principal.getName());
        model.addAttribute("rates", new RateDto());
        return "rate-info";
    }

    @PostMapping("/sendRate")
    public String sendRate(@ModelAttribute RateDto rateDto) {
        AppUser loggedUser = timeService.getLoggedUser();
        boolean rates = userDbService.existRateForUser(loggedUser);
        rateDto.setAppUser(timeService.getLoggedUser());
        if(rates) {
            return "errorRate";
        }
        rateRepository.save(serviceMapper.mapToRate(rateDto));
        return "redirect:/getRate";
    }

    @PutMapping("/updateRate")
    public String updateRate(@ModelAttribute RateDto rateDto) {
        serviceMapper.mapToRateDto(rateRepository.save(serviceMapper.mapToRate(rateDto)));
        return "rate-info";
    }

    @GetMapping("/deleteRate/{id}")
    public String deleteRate(@PathVariable Long id) {
        rateRepository.deleteById(id);
        return "index";
    }

    @GetMapping("/showRate")
    public String showRates(Model model) {
        rateRepository.findByAppUser(timeService.getLoggedUser());
        return "rate-info";
    }
}
