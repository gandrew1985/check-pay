package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.exception.NotFoundException;
import com.andrzej.payroll.mapper.UserMapper;
import com.andrzej.payroll.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppAdminController {

    private UserMapper userMapper;
    private AppUserRepository appUserRepository;

    @Autowired
    public AppAdminController(UserMapper userMapper, AppUserRepository appUserRepository) {
        this.userMapper = userMapper;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/createUser")
    public void createUser(@RequestBody AppUserDto appUserDto) {
        appUserRepository.save(userMapper.mapToAppUser(appUserDto));
    }

    @PutMapping("/editUser")
    public AppUserDto editUser(@RequestBody AppUserDto appUserDto) {
        return userMapper.mapToAppUserDto(appUserRepository.save(userMapper.mapToAppUser(appUserDto)));
    }

    @GetMapping("/getUser/{id}")
    public AppUserDto getUser(@PathVariable Long id) throws NotFoundException {
        return userMapper.mapToAppUserDto(appUserRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist")));
    }

    @GetMapping("/getAllUsers")
    public List<AppUserDto> getAll() {

        return userMapper.mapToAppUserDtoList(appUserRepository.findAll());
    }

    @DeleteMapping("/deleteUser/{id}")
    public void delete(@PathVariable Long id) {
        appUserRepository.deleteById(id);
    }
}
