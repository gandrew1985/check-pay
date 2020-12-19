package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.domain.Workday;
import com.andrzej.payroll.mapper.UserMapper;
import com.andrzej.payroll.service.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableWebSecurity
public class LoginController {

    private UserDbService userDbService;
    private UserMapper userMapper;

    @Autowired
    public LoginController(UserDbService userDbService, UserMapper userMapper) {
        this.userDbService = userDbService;
        this.userMapper = userMapper;
    }

    @GetMapping("/home")
    public String getHome(Model model, Principal principal) {
        List<Workday> workdaysList = new ArrayList<>();
        model.addAttribute("newAppUser", new AppUserDto());
        model.addAttribute("userLogged",principal.getName());
        model.addAttribute("chosenDay", new Workday());
        model.addAttribute("list", workdaysList);
        return "home";
    }

    @GetMapping("/postLogin")
    public String logIn(@ModelAttribute("appUser") AppUserDto appUserDto, Model model) {
        model.addAttribute("newAppUser", new AppUserDto());
        return "home";
    }

    @GetMapping("/loginPage")
    public String get(Model model) {
        model.addAttribute("newAppUser", new AppUserDto());
        return "signup";
    }

    @GetMapping("/getRegister")
    public String register(Model model) {
        model.addAttribute("newRegAppUser", new AppUserDto());
        model.addAttribute("role", "ROLE_USER");
        return "register";
    }

    @PostMapping("/postRegUser")
    public String register(@ModelAttribute AppUserDto appUserDto) {
        userDbService.addUser(userMapper.mapToAppUser(appUserDto));
        System.out.println(userMapper.mapToAppUser(appUserDto));
        return "redirect:/loginPage";
    }
}
