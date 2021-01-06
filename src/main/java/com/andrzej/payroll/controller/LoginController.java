package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.mapper.UserMapper;
import com.andrzej.payroll.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableWebSecurity
@RequiredArgsConstructor
@RequestMapping("/")
public class LoginController {

    private final DbService dbService;
    private final UserMapper userMapper;

    @GetMapping("/home")
    public String getHome(Model model, Principal principal) {
        List<Workday> workdaysList = new ArrayList<>();
        model.addAttribute("newAppUser", new AppUserDto());
        model.addAttribute("userLogged",principal.getName());
        model.addAttribute("chosenDay", new Workday());
        model.addAttribute("list", workdaysList);
        return "index";
    }

    @PostMapping
    public String logIn(@ModelAttribute("appUser") AppUserDto appUserDto, Model model) {
        model.addAttribute("newAppUser", new AppUserDto());
        return "index";
    }

    @GetMapping
    public String showLoginPage(Model model) {
        model.addAttribute("newAppUser", new AppUserDto());
        return "signup";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("newRegAppUser", new AppUserDto());
        model.addAttribute("role", "ROLE_USER");
        return "register";
    }

    @PostMapping("/register")
    public String sendRegister(@ModelAttribute AppUserDto appUserDto) {
        dbService.addUser(userMapper.mapToAppUser(appUserDto));
        return "redirect:/";
    }
}
