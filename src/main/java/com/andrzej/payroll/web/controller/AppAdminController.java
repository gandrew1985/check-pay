package com.andrzej.payroll.web.controller;

import com.andrzej.payroll.persist.mapper.UserMapper;
import com.andrzej.payroll.persist.service.AppUserService;
import com.andrzej.payroll.web.dtos.AppUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AppAdminController {

    private final UserMapper userMapper;
    private final AppUserService appUserService;

    @PostMapping("/users/add")
    public void addUser(@RequestBody AppUserDto appUserDto) {
        appUserService.addUser(userMapper.mapToAppUser(appUserDto));
    }

    @PutMapping("/users/{userId}/edit")
    public AppUserDto updateUser(@ModelAttribute AppUserDto appUserDto, @PathVariable long userId) {
        return userMapper.mapToAppUserDto(appUserService.editUser(userMapper.mapToAppUser(appUserDto)));
    }

    @GetMapping("/users/{userId}")
    public AppUserDto getUser(@PathVariable Long userId) {

        return userMapper.mapToAppUserDto(appUserService.findById(userId));
    }

    @GetMapping("/users")
    public List<AppUserDto> getAllUsers() {
        return userMapper.mapToAppUserDtoList(appUserService.findAllUsers());
    }

    @DeleteMapping("/users/{userId}/delete")
    public void deleteUser(@PathVariable Long userId) {
        appUserService.deleteUser(userId);
    }
}
