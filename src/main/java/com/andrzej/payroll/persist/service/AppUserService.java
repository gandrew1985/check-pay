package com.andrzej.payroll.persist.service;

import com.andrzej.payroll.core.exception.NotFoundException;
import com.andrzej.payroll.core.security.WebSecurityConfig;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final WebSecurityConfig securityConfig;

    public void addUser(AppUser appUser) {
        appUser.setPassword(securityConfig.passwordEncoder().encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }

    public AppUser editUser(AppUser appUser) {
        AppUser userEdited = appUserRepository.findById(appUser.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        userEdited.setUsername(appUser.getUsername());
        userEdited.setPassword(securityConfig.passwordEncoder().encode(appUser.getPassword()));
        userEdited.setRole(appUser.getRole());
        return userEdited;
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
    }

    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    public AppUser findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public AppUser findUserByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username);
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
    }
}