package com.andrzej.payroll.web.controller;

import com.andrzej.payroll.core.exception.UserAlreadyExistException;
import com.andrzej.payroll.core.security.DefaultUserService;
import com.andrzej.payroll.persist.entity.Workday;
import com.andrzej.payroll.persist.mapper.UserMapper;
import com.andrzej.payroll.web.dtos.AppUserDto;
import com.andrzej.payroll.web.dtos.WorkdayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableWebSecurity
@RequiredArgsConstructor
@RequestMapping("/")
public class LoginController {

    private final DefaultUserService defaultUserService;
    private final UserMapper userMapper;

    @GetMapping("/home")
    public String getHome(Model model, Principal principal) {
        List<Workday> workdaysList = new ArrayList<>();
        model.addAttribute("newAppUser", new AppUserDto());
        model.addAttribute("userLogged", principal.getName());
        model.addAttribute("chosenDay", new WorkdayDto());
        model.addAttribute("list", workdaysList);
        return "index";
    }

    @PostMapping("/")
    public String logIn(@ModelAttribute("appUser") @Valid AppUserDto appUserDto, Model model) {
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
    public String sendRegister(@ModelAttribute @Valid AppUserDto appUserDto) {
        try {
            defaultUserService.register(userMapper.mapToAppUser(appUserDto));
        } catch (UserAlreadyExistException e) {
            return "register";
        }
        //appUserService.addUser(userMapper.mapToAppUser(appUserDto));
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logOut() {
        return "signup";
    }
}
