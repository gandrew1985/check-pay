package com.andrzej.payroll.mapper;

import com.andrzej.payroll.configuration.WebSecurityConfig;
import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.AppUserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private WebSecurityConfig securityConfig;

    public UserMapper(WebSecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    public AppUser mapToAppUser(AppUserDto appUserDto) {
        return new AppUser(
                appUserDto.getId(),
                appUserDto.getUsername(),
                appUserDto.getPassword(),
                appUserDto.getEmail(),
                appUserDto.getRole());
    }

    public AppUserDto mapToAppUserDto(AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getUsername(),
                securityConfig.passwordEncoder().encode(appUser.getPassword()),
                appUser.getEmail(),
                appUser.getRole());
    }

    public List<AppUserDto> mapToAppUserDtoList(List<AppUser> appUserList) {
        return appUserList.stream()
                .map(appUser -> new AppUserDto(appUser.getId(), appUser.getUsername(),
                        securityConfig.passwordEncoder().encode(appUser.getPassword()),
                        appUser.getEmail(), appUser.getRole()))
                .collect(Collectors.toList());
    }
}
